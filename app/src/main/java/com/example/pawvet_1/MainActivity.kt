package com.example.pawvet_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.pawvet_1.ui.components.PawVetAppShell
import com.example.pawvet_1.ui.notifications.NotificationHelper
import com.example.pawvet_1.ui.theme.PawVet_1Theme

class MainActivity : ComponentActivity() {

    private var pendingRoute by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        pendingRoute = intent?.getStringExtra(NotificationHelper.EXTRA_TARGET_ROUTE)

        setContent {
            PawVet_1Theme {
                val navController = rememberNavController()
                PawVetAppShell(
                    navController = navController,
                    pendingRoute = pendingRoute,
                    onPendingRouteConsumed = { pendingRoute = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingRoute = intent.getStringExtra(NotificationHelper.EXTRA_TARGET_ROUTE)
    }
}
