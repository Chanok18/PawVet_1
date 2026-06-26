package com.example.pawvet_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.pawvet_1.ui.components.PawVetAppShell
import com.example.pawvet_1.ui.theme.PawVet_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawVet_1Theme {
                val navController = rememberNavController()
                PawVetAppShell(navController = navController)
            }
        }
    }
}
