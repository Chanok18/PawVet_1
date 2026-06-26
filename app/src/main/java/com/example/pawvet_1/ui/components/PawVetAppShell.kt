package com.example.pawvet_1.ui.components

import android.Manifest
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pawvet_1.PawVetApplication
import com.example.pawvet_1.core.hasInternetConnection
import com.example.pawvet_1.navigation.PawVetNavGraph
import com.example.pawvet_1.navigation.Screen
import com.example.pawvet_1.ui.notifications.NotificationHelper
import com.example.pawvet_1.ui.screens.login.LoginScreen
import com.example.pawvet_1.ui.screens.register.RegisterScreen
import com.example.pawvet_1.ui.theme.PawVetBackground
import com.example.pawvet_1.ui.theme.PawVetBodyFont
import com.example.pawvet_1.ui.theme.PawVetBorder
import com.example.pawvet_1.ui.theme.PawVetCoralGlow
import com.example.pawvet_1.ui.theme.PawVetPrimary
import com.example.pawvet_1.ui.theme.PawVetTextSecondary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private data class NavHudItem(
    val label: String,
    val selected: (NavDestination?) -> Boolean,
    val onClick: () -> Unit,
    val icon: @Composable (Boolean) -> Unit
)

@Composable
fun PawVetAppShell(
    navController: NavHostController,
    pendingRoute: String? = null,
    onPendingRouteConsumed: () -> Unit = {}
) {
    val context = LocalContext.current
    val appContainer = remember(context) {
        (context.applicationContext as PawVetApplication).container
    }
    val sessionManager = remember {
        appContainer.sessionManager
    }
    val sessionState by sessionManager.sessionState.collectAsState()
    val scope = rememberCoroutineScope()
    var authScreen by rememberSaveable { mutableStateOf(AuthScreen.Login.name) }
    var authError by rememberSaveable { mutableStateOf<String?>(null) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        sessionManager.restoreSession()
    }

    LaunchedEffect(sessionState.isLoggedIn) {
        if (sessionState.isLoggedIn && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (!sessionState.isLoggedIn) {
        PawVetAmbientBackground()
        when (AuthScreen.valueOf(authScreen)) {
            AuthScreen.Login -> LoginScreen(
                onLoginClick = { email, password ->
                    if (!context.hasInternetConnection()) {
                        authError = "Tu dispositivo no tiene conexión a internet en este momento."
                        return@LoginScreen
                    }
                    scope.launchWithResult(
                        action = { sessionManager.login(email, password) },
                        onResult = { result -> authError = if (result.success) null else result.message }
                    )
                },
                onRegisterClick = {
                    authError = null
                    authScreen = AuthScreen.Register.name
                },
                errorMessage = authError
            )

            AuthScreen.Register -> RegisterScreen(
                onRegisterClick = { name, email, password ->
                    if (!context.hasInternetConnection()) {
                        authError = "Tu dispositivo no tiene conexión a internet en este momento."
                        return@RegisterScreen
                    }
                    scope.launchWithResult(
                        action = { sessionManager.register(name, email, password) },
                        onResult = { result -> authError = if (result.success) null else result.message }
                    )
                },
                onBackToLogin = {
                    authError = null
                    authScreen = AuthScreen.Login.name
                },
                errorMessage = authError
            )
        }
        return
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(sessionState.isLoggedIn, pendingRoute) {
        if (sessionState.isLoggedIn && !pendingRoute.isNullOrBlank()) {
            navController.navigate(pendingRoute) {
                launchSingleTop = true
            }
            onPendingRouteConsumed()
        }
    }

    val items = listOf(
        NavHudItem(
            label = "Inicio",
            selected = { destination ->
                destination.isOnRoute(Screen.Home.route) ||
                    destination.isOnRoute(Screen.Servicios.route) ||
                    destination.isOnRoute("servicio_form")
            },
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = false
                    }
                    launchSingleTop = true
                    restoreState = false
                }
            }
        ) { selected ->
            Icon(
                Icons.Default.Home,
                contentDescription = null,
                tint = if (selected) PawVetPrimary else PawVetTextSecondary,
                modifier = Modifier.size(19.dp)
            )
        },
        NavHudItem(
            label = "Citas",
            selected = { destination ->
                destination.isOnRoute(Screen.Citas.route) || destination.isOnRoute("cita_form")
            },
            onClick = {
                navController.navigate(Screen.Citas.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) { selected ->
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = if (selected) PawVetPrimary else PawVetTextSecondary,
                modifier = Modifier.size(19.dp)
            )
        },
        NavHudItem(
            label = "Chat IA",
            selected = { destination -> destination.isOnRoute(Screen.ConsultasRapidas.route) },
            onClick = {
                navController.navigate(Screen.ConsultasRapidas.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) { selected ->
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                tint = if (selected) PawVetPrimary else PawVetTextSecondary,
                modifier = Modifier.size(19.dp)
            )
        },
        NavHudItem(
            label = "Perfil",
            selected = { destination -> destination.isOnRoute(Screen.Perfil.route) || destination.isOnRoute("mascota_") },
            onClick = {
                navController.navigate(Screen.Perfil.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) { selected ->
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = if (selected) PawVetPrimary else PawVetTextSecondary,
                modifier = Modifier.size(19.dp)
            )
        }
    )

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            PawVetBottomHud(
                items = items,
                currentDestination = currentDestination
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PawVetBackground)
        ) {
            PawVetAmbientBackground()
            Box(modifier = Modifier.padding(innerPadding)) {
                PawVetNavGraph(
                    navController = navController,
                    currentUserName = sessionState.userName,
                    onLogout = {
                        sessionManager.logout()
                        authScreen = AuthScreen.Login.name
                        authError = null
                        NotificationHelper.cancelAllScheduledReminders(context)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PawVetAmbientBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PawVetBackground, Color(0xFFF8FCFA))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = 180.dp, y = (-70).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFB6EBEA).copy(alpha = 0.24f), Color.Transparent),
                        center = Offset(0.6f, 0.35f),
                        radius = 520f
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-90).dp, y = 560.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(PawVetCoralGlow.copy(alpha = 0.22f), Color.Transparent),
                        radius = 460f
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun PawVetBottomHud(
    items: List<NavHudItem>,
    currentDestination: NavDestination?
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, PawVetBorder.copy(alpha = 0.8f)),
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEach { item ->
                val isSelected = item.selected(currentDestination)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { item.onClick() }
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 36.dp, height = 34.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) PawVetPrimary.copy(alpha = 0.12f) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        item.icon(isSelected)
                    }
                    Text(
                        text = item.label,
                        color = if (isSelected) PawVetPrimary else PawVetTextSecondary,
                        fontFamily = PawVetBodyFont,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

private fun NavDestination?.isOnRoute(prefix: String): Boolean {
    return this?.hierarchy?.any { destination ->
        destination.route?.startsWith(prefix) == true
    } == true
}

private enum class AuthScreen {
    Login,
    Register
}

private fun CoroutineScope.launchWithResult(
    action: suspend () -> com.example.pawvet_1.data.session.AuthResult,
    onResult: (com.example.pawvet_1.data.session.AuthResult) -> Unit
) {
    launch {
        onResult(action())
    }
}
