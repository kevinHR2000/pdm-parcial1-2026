package com.example.pdm_parcial1_2026.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pdm_parcial1_2026.R
import com.example.pdm_parcial1_2026.ui.theme.FeedbackCorrecto
import com.example.pdm_parcial1_2026.ui.theme.FeedbackIncorrecto
import com.example.pdm_parcial1_2026.viewmodel.GameViewModel

/*
  Pantalla principal del juego.
  Solo se encarga de mostrar el estado y enviar eventos al ViewModel (MVVM).
  onGameOver es una lambda: la pantalla no decide a dónde navegar, eso lo hace el NavHost.
 */
@Composable
fun GameScreen(
    onGameOver: (Int) -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    // Observamos todos los StateFlow del ViewModel.
    // collectAsState() suscribe la UI: cada cambio dispara una recomposición.
    val colorActual by viewModel.colorActual.collectAsState()
    val opciones by viewModel.opciones.collectAsState()
    val puntaje by viewModel.puntaje.collectAsState()
    val tiempoRestante by viewModel.tiempoRestante.collectAsState()
    val juegoTerminado by viewModel.juegoTerminado.collectAsState()
    val feedback by viewModel.feedback.collectAsState()

    // LaunchedEffect reacciona cuando juegoTerminado cambia.
    // Cuando el ViewModel marca el juego como terminado, navegamos a Result.
    LaunchedEffect(juegoTerminado) {
        if (juegoTerminado) onGameOver(puntaje)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra superior: tiempo restante y puntaje
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.juego_tiempo, tiempoRestante),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.juego_puntaje, puntaje),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.juego_pregunta),
                style = MaterialTheme.typography.titleLarge
            )

            // ANIMACIÓN 1: AnimatedContent anima la transición cuando colorActual cambia.
            // El cuadro nuevo entra con fade + scale, el viejo sale con fade + scale.
            AnimatedContent(
                targetState = colorActual,
                transitionSpec = {
                    (fadeIn(tween(300)) + scaleIn(tween(300))) togetherWith
                        (fadeOut(tween(300)) + scaleOut(tween(300)))
                },
                label = "color_box"
            ) { color ->
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color.composeColor)
                )
            }

            // ANIMACIÓN 2: AnimatedVisibility muestra/oculta el texto de feedback.
            // El feedback aparece 500ms y luego desaparece (controlado por el ViewModel).
            AnimatedVisibility(
                visible = feedback != null,
                enter = fadeIn(tween(200)) + scaleIn(tween(200)),
                exit = fadeOut(tween(200)) + scaleOut(tween(200))
            ) {
                Text(
                    text = stringResource(
                        if (feedback == "correcto") R.string.juego_correcto
                        else R.string.juego_incorrecto
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (feedback == "correcto") FeedbackCorrecto else FeedbackIncorrecto,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Grid 2x2 de botones con las 4 opciones de color.
            // Los botones se deshabilitan mientras se muestra el feedback (evita spam).
            opciones.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    fila.forEach { color ->
                        Button(
                            onClick = { viewModel.onColorSeleccionado(color) },
                            modifier = Modifier.weight(1f),
                            enabled = feedback == null
                        ) {
                            Text(text = color.displayName)
                        }
                    }
                }
            }
        }
    }
}
