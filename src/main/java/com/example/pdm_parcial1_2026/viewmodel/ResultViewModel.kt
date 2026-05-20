package com.example.pdm_parcial1_2026.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel de la pantalla de Resultado.
 * Hereda de AndroidViewModel porque necesita el Context para SharedPreferences.
 *
 * Responsabilidades:
 *  - Leer / actualizar el récord histórico (puntaje más alto).
 *  - Mantener el historial de las últimas 10 partidas (con número de partida).
 */
class ResultViewModel(application: Application) : AndroidViewModel(application) {

    // SharedPreferences: archivo "juego_prefs" donde guardamos récord e historial.
    private val prefs = application.getSharedPreferences("juego_prefs", Context.MODE_PRIVATE)

    // Cada entrada del historial tiene número de partida + puntaje (rúbrica lo exige).
    data class Partida(val numero: Int, val puntaje: Int)

    // Estado privado mutable + estado público de solo lectura (patrón StateFlow).
    private val _record = MutableStateFlow(prefs.getInt("record", 0))
    val record = _record.asStateFlow()

    private val _historial = MutableStateFlow(cargarHistorial())
    val historial = _historial.asStateFlow()

    /**
     * Se llama una vez cuando ResultScreen aparece. Guarda la partida actual:
     *  - Actualiza el récord si el puntaje lo supera.
     *  - Incrementa el contador de partidas totales.
     *  - Agrega la partida al historial (máximo 10 entradas, más recientes primero).
     */
    fun guardarPartida(puntaje: Int) {
        // Actualizar récord si corresponde
        if (puntaje > _record.value) {
            prefs.edit().putInt("record", puntaje).apply()
            _record.value = puntaje
        }

        // Incrementar número total de partidas jugadas
        val totalPartidas = prefs.getInt("total_partidas", 0) + 1
        prefs.edit().putInt("total_partidas", totalPartidas).apply()

        // Agregar al historial (la más reciente arriba)
        val nuevaPartida = Partida(numero = totalPartidas, puntaje = puntaje)
        val nuevoHistorial = (listOf(nuevaPartida) + _historial.value).take(10)

        // Serializar como "1|10,2|5,3|8" para guardarlo en SharedPreferences
        val serializado = nuevoHistorial.joinToString(",") { "${it.numero}|${it.puntaje}" }
        prefs.edit().putString("historial", serializado).apply()

        _historial.value = nuevoHistorial
    }

    /**
     * Lee el historial desde SharedPreferences y lo convierte a List<Partida>.
     * Formato almacenado: "numero|puntaje,numero|puntaje,..."
     */
    private fun cargarHistorial(): List<Partida> {
        val raw = prefs.getString("historial", "") ?: ""
        return raw.split(",")
            .filter { it.isNotBlank() }
            .mapNotNull { entrada ->
                val partes = entrada.split("|")
                if (partes.size == 2) {
                    Partida(numero = partes[0].toInt(), puntaje = partes[1].toInt())
                } else null
            }
    }
}
