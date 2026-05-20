package com.example.pdm_parcial1_2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pdm_parcial1_2026.ui.screens.GameScreen
import com.example.pdm_parcial1_2026.ui.screens.ResultScreen
import com.example.pdm_parcial1_2026.ui.screens.WelcomeScreen

/**
 * NavHost de la aplicación. Define las 3 rutas y cómo se conectan entre sí.
 *
 * Cada pantalla recibe lambdas de navegación (NO conoce a NavController directamente),
 * lo que mantiene a las pantallas desacopladas y testeables.
 */
@Composable
fun AppNavigation() {
    // rememberNavController guarda la instancia entre recomposiciones (no se resetea).
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "bienvenido"
    ) {
        // Ruta 1: pantalla de bienvenida
        composable("bienvenido") {
            WelcomeScreen(
                onStartClick = { navController.navigate("juego") }
            )
        }

        // Ruta 2: pantalla del juego. Al terminar, navega a resultado con el puntaje.
        composable("juego") {
            GameScreen(
                onGameOver = { puntaje ->
                    navController.navigate("resultado/$puntaje") {
                        // popUpTo limpia el backstack hasta "bienvenido" para que
                        // "back" desde Result regrese a la bienvenida (no al juego).
                        popUpTo("bienvenido") { inclusive = false }
                    }
                }
            )
        }

        // Ruta 3: pantalla de resultado. El puntaje viene como argumento en la ruta.
        composable("resultado/{puntaje}") { backStackEntry ->
            val puntaje = backStackEntry.arguments?.getString("puntaje")?.toIntOrNull() ?: 0
            ResultScreen(
                puntaje = puntaje,
                onJugarDeNuevo = {
                    navController.navigate("juego") {
                        popUpTo("bienvenido") { inclusive = false }
                    }
                },
                onVolverInicio = {
                    navController.popBackStack("bienvenido", inclusive = false)
                }
            )
        }
    }
}
