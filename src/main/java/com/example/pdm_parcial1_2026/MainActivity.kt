package com.example.pdm_parcial1_2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pdm_parcial1_2026.ui.navigation.AppNavigation
import com.example.pdm_parcial1_2026.ui.screens.GameScreen
import com.example.pdm_parcial1_2026.ui.screens.ResultScreen
import com.example.pdm_parcial1_2026.ui.screens.WelcomeScreen
import com.example.pdm_parcial1_2026.ui.theme.Pdm_parcial1_2026Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pdm_parcial1_2026Theme {
                AppNavigation()
                //WelcomeScreen()
                //GameScreen()
                //ResultScreen()

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pdm_parcial1_2026Theme {
        AppNavigation()
        //GameScreen
        //WelcomeScreen()
        //ResultScreen()
    }
}