package com.example.pdm_parcial1_2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pdm_parcial1_2026.R
import com.example.pdm_parcial1_2026.ui.theme.Pdm_parcial1_2026Theme

// La pantalla recibe una lambda: NO sabe a dónde navega.
// Quien la llame (el NavHost) decide qué hacer al presionar "Iniciar Juego".
// Esto mantiene la pantalla desacoplada -> principio MVVM/separación de UI.
@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
){
    Scaffold {
        innerPadding -> Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(innerPadding)//espacio reservado
                .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ){
            Text(//Primero
                text = stringResource(R.string.bienvenido_titulo),//Juegos de Colores
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.bienvenido_subtitulo),
                style = MaterialTheme.typography.titleMedium

            )
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ){
                    Text(
                        text = stringResource(R.string.reglas_titulo),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.regla_uno)
                    )
                    Text(
                        text = stringResource(R.string.regla_dos)
                    )
                    Text(
                        text = stringResource(R.string.regla_tres)
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = stringResource(R.string.boton_iniciar)
                )
            }
        }
    }

}



