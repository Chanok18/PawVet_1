package com.example.pawvet_1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pawvet_1.PawVetApplication
import com.example.pawvet_1.data.PawVetDatabase
import com.example.pawvet_1.data.remote.RetrofitClient
import com.example.pawvet_1.data.repository.AuthRepository
import com.example.pawvet_1.data.repository.BreedsRepository
import com.example.pawvet_1.data.repository.CitaRepository
import com.example.pawvet_1.data.repository.MascotaRepository
import com.example.pawvet_1.data.repository.ServicioRepository
import com.example.pawvet_1.ui.screens.home.HomeScreen
import com.example.pawvet_1.ui.screens.login.LoginScreen
import com.example.pawvet_1.ui.screens.register.RegisterScreen
import com.example.pawvet_1.ui.screens.mascotas.MascotaDetalleScreen
import com.example.pawvet_1.ui.screens.mascotas.MascotaFormScreen
import com.example.pawvet_1.ui.screens.citas.CitaFormScreen
import com.example.pawvet_1.ui.screens.perfil.PerfilScreen
import com.example.pawvet_1.ui.screens.consultas.ConsultasRapidasScreen
import com.example.pawvet_1.ui.screens.servicios.ServiciosScreen
import com.example.pawvet_1.ui.screens.servicios.ServicioFormScreen
import com.example.pawvet_1.ui.viewmodel.AuthViewModel
import com.example.pawvet_1.ui.viewmodel.BreedsViewModel
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import com.example.pawvet_1.ui.viewmodel.ServicioViewModel
import com.example.pawvet_1.ui.viewmodel.ViewModelFactory
import com.example.pawvet_1.ui.AppViewModelProvider
import com.example.pawvet_1.notifications.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PawVetNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val database = PawVetDatabase.getDatabase(context)
    val firestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val notificationHelper = NotificationHelper(context)
    
    // Repositorios
    val authRepo = AuthRepository(firebaseAuth)
    val mascotaRepo = MascotaRepository(database.mascotaDao(), firestore, firebaseAuth)
    val citaRepo = CitaRepository(database.citaDao(), firestore, firebaseAuth)
    val servicioRepo = ServicioRepository(database.servicioDao())
    val breedsRepo = BreedsRepository(RetrofitClient.dogApiService)
    
    val authViewModel: AuthViewModel = viewModel(factory = ViewModelFactory(authRepo))
    val authState by authViewModel.uiState.collectAsState()

    val startDest = if (authState.usuario != null) Screen.Home.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDest) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onCitasClick = { navController.navigate(Screen.CitaForm.createRoute(0)) },
                onServiciosClick = { navController.navigate(Screen.Servicios.route) },
                onConsultasClick = { navController.navigate(Screen.ConsultasRapidas.route) },
                onPerfilClick = { navController.navigate(Screen.Perfil.route) }
            )
        }

        composable(Screen.Perfil.route) {
            PerfilScreen(
                authViewModel = authViewModel,
                mascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo)),
                citaViewModel = viewModel(factory = ViewModelFactory(
                    repository = citaRepo,
                    notificationHelper = notificationHelper
                )),
                servicioViewModel = viewModel(factory = ViewModelFactory(servicioRepo)),
                onLogout = {
                    authViewModel.cerrarSesion()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
                onMascotaClick = { id -> navController.navigate(Screen.MascotaDetalle.createRoute(id)) },
                onAddMascotaClick = { navController.navigate(Screen.MascotaForm.createRoute(0)) },
                onEditMascotaClick = { id -> navController.navigate(Screen.MascotaForm.createRoute(id)) },
                onEditCitaClick = { id -> navController.navigate(Screen.CitaForm.createRoute(id)) }
            )
        }

        composable(Screen.MascotaDetalle.route, arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            MascotaDetalleScreen(
                mascotaId = id,
                viewModel = viewModel(factory = ViewModelFactory(mascotaRepo)),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MascotaForm.route, arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            MascotaFormScreen(
                mascotaId = id,
                viewModel = viewModel(factory = ViewModelFactory(mascotaRepo)),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CitaForm.route, arguments = listOf(navArgument("citaId") { type = NavType.IntType })) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("citaId") ?: 0
            CitaFormScreen(
                citaId = id,
                citaViewModel = viewModel(factory = ViewModelFactory(
                    repository = citaRepo,
                    notificationHelper = notificationHelper
                )),
                mascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo)),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Servicios.route) {
            ServiciosScreen(
                viewModel = viewModel(factory = ViewModelFactory(breedsRepo)), 
                onNavigateToForm = { navController.navigate(Screen.ServicioForm.route) }, 
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ServicioForm.route) {
            ServicioFormScreen(
                viewModel = viewModel(factory = ViewModelFactory(servicioRepo)), 
                mascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo)), 
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ConsultasRapidas.route) {
            ConsultasRapidasScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory), 
                onBack = { navController.popBackStack() }
            )
        }
    }
}
