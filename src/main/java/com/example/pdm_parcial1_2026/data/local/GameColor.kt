package com.example.pdm_parcial1_2026.data.local

import androidx.compose.ui.graphics.Color
import com.example.pdm_parcial1_2026.ui.theme.ColorAmarillo
import com.example.pdm_parcial1_2026.ui.theme.ColorAzul
import com.example.pdm_parcial1_2026.ui.theme.ColorBlanco
import com.example.pdm_parcial1_2026.ui.theme.ColorNaranja
import com.example.pdm_parcial1_2026.ui.theme.ColorRojo
import com.example.pdm_parcial1_2026.ui.theme.ColorVerde

class GameColor(
    val displayName: String,
    val composeColor: Color
) {
    companion object {
        val todos = listOf(
            GameColor("Rojo", ColorRojo),
            GameColor("Azul", ColorAzul),
            GameColor("Verde", ColorVerde),
            GameColor("Amarillo", ColorAmarillo),
            GameColor("Blanco", ColorBlanco),
            GameColor("Naranja", ColorNaranja)
        )
    }
}
