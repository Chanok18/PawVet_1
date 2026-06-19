package com.example.pawvet_1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pawvet_1.data.PawVetDatabase
import com.example.pawvet_1.data.remote.RetrofitClient
import com.example.pawvet_1.data.repository.BreedsRepository
import com.example.pawvet_1.data.repository.MascotaRepository
import com.example.pawvet_1.ui.screens.home.HomeScreen
import com.example.pawvet_1.ui.screens.mascotas.MascotaFormScreen
import com.example.pawvet_1.ui.screens.perfil.PerfilScreen
import com.example.pawvet_1.ui.screens.consultas.ConsultasRapidasScreen
import com.example.pawvet_1.ui.viewmodel.BreedsViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.data.repository.CitaRepository
import com.example.pawvet_1.ui.viewmodel.ViewModelFactory

@Composable
fun PawVetNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val database = PawVetDatabase.getDatabase(context)
    
    // Repositorios necesarios para las pantallas simplificadas
    val mascotaRepo = MascotaRepository(database.mascotaDao())
    val breedsRepo = BreedsRepository(RetrofitClient.dogApiService)
    val citaRepo = CitaRepository(database.citaDao()) // Se mantiene para el Perfil si es necesario

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 1. DASHBOARD (Pantalla de Inicio)
        composable(Screen.Home.route) {
            HomeScreen(
                onCitasClick = { /* Simplificado: Podría ir a Perfil o una nota */ },
                onServiciosClick = { /* Simplificado */ },
                onConsultasClick = { navController.navigate(Screen.ConsultasRapidas.route) },
                onPerfilClick = { navController.navigate(Screen.Perfil.route) }
            )
        }

        // 2. PERFIL (Lista de Mascotas - El Corazón del CRUD)
        composable(Screen.Perfil.route) {
            val mascotaVm: MascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo))
            val citaVm: CitaViewModel = viewModel(factory = ViewModelFactory(citaRepo))

            PerfilScreen(
                mascotaViewModel = mascotaVm,
                citaViewModel = citaVm,
                onBack = { navController.popBackStack() },
                onMascotaClick = { id -> navController.navigate(Screen.MascotaForm.createRoute(id)) },
                onAddMascotaClick = { navController.navigate(Screen.MascotaForm.createRoute(0)) },
                onEditMascotaClick = { id -> navController.navigate(Screen.MascotaForm.createRoute(id)) },
                onEditCitaClick = { /* Simplificado */ }
            )
        }

        // 3. FORMULARIO DE MASCOTA (Crear y Editar)
        composable(
            route = Screen.MascotaForm.route,
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            val viewModel: MascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo))
            MascotaFormScreen(
                mascotaId = id, 
                viewModel = viewModel, 
                onBack = { navController.popBackStack() }
            )
        }

        // 4. CONSULTAS RÁPIDAS (API Retrofit)
        composable(Screen.ConsultasRapidas.route) {
            val viewModel: BreedsViewModel = viewModel(factory = ViewModelFactory(breedsRepo))
            ConsultasRapidasScreen(
                viewModel = viewModel, 
                onBack = { navController.popBackStack() }
            )
        }
    }
}
