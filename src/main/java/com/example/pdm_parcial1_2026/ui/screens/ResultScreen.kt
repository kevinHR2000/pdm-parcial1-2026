package com.example.pdm_parcial1_2026.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pdm_parcial1_2026.R
import com.example.pdm_parcial1_2026.viewmodel.ResultViewModel

/**
  Pantalla final con puntaje, récord histórico y LazyColumn del historial.
 Recibe el puntaje desde la navegación (NavHost lo pasa como argumento).
  Las acciones de navegación se reciben como lambdas (UI desacoplada).
 */
@Composable
fun ResultScreen(
    puntaje: Int,
    onJugarDeNuevo: () -> Unit,
    onVolverInicio: () -> Unit,
    viewModel: ResultViewModel = viewModel()
) {
    // Observamos los StateFlow del ViewModel; collectAsState recompose cuando cambian.
    val record by viewModel.record.collectAsState()
    val historial by viewModel.historial.collectAsState()

    // Guardamos la partida una sola vez al entrar a la pantalla.
    LaunchedEffect(Unit) {
        viewModel.guardarPartida(puntaje)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.resultado_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Card con puntaje actual y récord histórico (de SharedPreferences).
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.resultado_tu_puntaje, puntaje),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.resultado_record, record),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.resultado_historial),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn = lista eficiente que solo dibuja los items visibles.
            // Cada item muestra número de partida y puntaje (requisito del parcial).
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historial) { partida ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(
                                R.string.resultado_partida,
                                partida.numero,
                                partida.puntaje
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onJugarDeNuevo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.resultado_jugar_de_nuevo))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onVolverInicio,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.resultado_volver_inicio))
            }
        }
    }
}
