package com.example.pdm_parcial1_2026.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdm_parcial1_2026.data.local.GameColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
  ViewModel del juego.
  Contiene TODA la lógica del juego (MVVM: la UI no decide nada, solo observa).

  Hereda de ViewModel para:
   - Sobrevivir a cambios de configuración (rotar pantalla NO reinicia el juego).
   - Tener acceso a viewModelScope, un CoroutineScope que se cancela al destruir
     el ViewModel (perfecto para el temporizador: no hay que cancelarlo a mano).
 */
class GameViewModel : ViewModel() {

    // Patrón estándar: _xxx privado mutable, xxx público de solo lectura.
    // Solo el ViewModel puede modificar el estado; la UI solo lo observa.

    private val _colorActual = MutableStateFlow(GameColor.todos.random())
    val colorActual = _colorActual.asStateFlow()

    private val _opciones = MutableStateFlow(generarOpciones(_colorActual.value))
    val opciones = _opciones.asStateFlow()

    private val _puntaje = MutableStateFlow(0)
    val puntaje = _puntaje.asStateFlow()

    private val _tiempoRestante = MutableStateFlow(30)
    val tiempoRestante = _tiempoRestante.asStateFlow()

    private val _juegoTerminado = MutableStateFlow(false)
    val juegoTerminado = _juegoTerminado.asStateFlow()

    // Feedback: null = sin mostrar; "correcto" o "incorrecto" durante 500ms.
    private val _feedback = MutableStateFlow<String?>(null)
    val feedback = _feedback.asStateFlow()

    init {
        // Arrancar el temporizador apenas se crea el ViewModel.
        iniciarTemporizador()
    }

    /*
      Temporizador de 30 segundos usando corrutinas.
      delay(1000L) suspende la corrutina 1 segundo sin bloquear el hilo principal.
      Cuando llega a 0 marca el juego como terminado y la UI navega a Result.
     */
    private fun iniciarTemporizador() {
        viewModelScope.launch {
            while (_tiempoRestante.value > 0) {
                delay(1000L)
                _tiempoRestante.value -= 1
            }
            _juegoTerminado.value = true
        }
    }

    /*
      Evento desde la UI cuando el usuario presiona uno de los 4 botones.
      - Si el juego terminó, ignora.
      - Si ya hay feedback mostrándose, ignora (evita spam de clics).
      - Suma punto si acierta; muestra feedback durante 500ms; luego avanza.
     */
    fun onColorSeleccionado(seleccionado: GameColor) {
        if (_juegoTerminado.value) return
        if (_feedback.value != null) return

        if (seleccionado.displayName == _colorActual.value.displayName) {
            _puntaje.value += 1
            _feedback.value = "correcto"
        } else {
            _feedback.value = "incorrecto"
        }

        viewModelScope.launch {
            delay(500L)
            _feedback.value = null
            siguienteRonda()
        }
    }

    // Genera un nuevo color objetivo y 4 nuevas opciones.
    private fun siguienteRonda() {
        _colorActual.value = GameColor.todos.random()
        _opciones.value = generarOpciones(_colorActual.value)
    }

    // Devuelve 4 opciones: la correcta + 3 incorrectas aleatorias, todas mezcladas.
    private fun generarOpciones(correcto: GameColor): List<GameColor> {
        val incorrectos = GameColor.todos
            .filter { it.displayName != correcto.displayName }
            .shuffled()
            .take(3)
        return (incorrectos + correcto).shuffled()
    }
}
