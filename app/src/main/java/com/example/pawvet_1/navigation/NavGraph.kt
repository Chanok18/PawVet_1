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
import com.example.pawvet_1.data.repository.CitaRepository
import com.example.pawvet_1.data.repository.MascotaRepository
import com.example.pawvet_1.data.repository.ServicioRepository
import com.example.pawvet_1.ui.screens.citas.CitasScreen
import com.example.pawvet_1.ui.screens.home.HomeScreen
import com.example.pawvet_1.ui.screens.mascotas.MascotaDetalleScreen
import com.example.pawvet_1.ui.screens.mascotas.MascotaFormScreen
import com.example.pawvet_1.ui.screens.citas.CitaFormScreen
import com.example.pawvet_1.ui.screens.perfil.PerfilScreen
import com.example.pawvet_1.ui.screens.consultas.ConsultasRapidasScreen
import com.example.pawvet_1.ui.screens.servicios.ServiciosScreen
import com.example.pawvet_1.ui.screens.servicios.ServicioFormScreen
import com.example.pawvet_1.ui.viewmodel.BreedsViewModel
import com.example.pawvet_1.ui.viewmodel.CitaViewModel
import com.example.pawvet_1.ui.viewmodel.MascotaViewModel
import com.example.pawvet_1.ui.viewmodel.ServicioViewModel
import com.example.pawvet_1.ui.viewmodel.ViewModelFactory

/**
 * GRAFO DE NAVEGACIÓN ACTUALIZADO
 */
@Composable
fun PawVetNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val database = PawVetDatabase.getDatabase(context)
    val mascotaRepo = MascotaRepository(database.mascotaDao())
    val citaRepo = CitaRepository(database.citaDao())
    val servicioRepo = ServicioRepository(database.servicioDao())
    val breedsRepo = BreedsRepository(RetrofitClient.dogApiService)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 1. DASHBOARD
        composable(Screen.Home.route) {
            HomeScreen(
                onCitasClick = { navController.navigate(Screen.Citas.route) },
                onServiciosClick = { navController.navigate(Screen.Servicios.route) },
                onConsultasClick = { navController.navigate(Screen.ConsultasRapidas.route) },
                onPerfilClick = { navController.navigate(Screen.Perfil.route) }
            )
        }

        composable(Screen.Citas.route) {
            val citaVm: CitaViewModel = viewModel(factory = ViewModelFactory(citaRepo))
            CitasScreen(
                viewModel = citaVm,
                onNavigateToForm = { id -> navController.navigate(Screen.CitaForm.createRoute(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        // 2. PERFIL
        composable(Screen.Perfil.route) {
            val mascotaVm: MascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo))
            val citaVm: CitaViewModel = viewModel(factory = ViewModelFactory(citaRepo))

            PerfilScreen(
                mascotaViewModel = mascotaVm,
                citaViewModel = citaVm,
                onBack = { navController.popBackStack() },
                onMascotaClick = { id: Int -> navController.navigate(Screen.MascotaDetalle.createRoute(id)) },
                onAddMascotaClick = { navController.navigate(Screen.MascotaForm.createRoute(0)) },
                onEditMascotaClick = { id: Int -> navController.navigate(Screen.MascotaForm.createRoute(id)) },
                onEditCitaClick = { id: Int -> navController.navigate(Screen.CitaForm.createRoute(id)) }
            )
        }

        // 3. DETALLE MASCOTA
        composable(
            route = Screen.MascotaDetalle.route,
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            val viewModel: MascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo))
            MascotaDetalleScreen(
                mascotaId = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 4. FORMULARIOS
        composable(
            route = Screen.MascotaForm.route,
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("mascotaId") ?: 0
            val viewModel: MascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo))
            MascotaFormScreen(mascotaId = id, viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.CitaForm.route,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("citaId") ?: 0
            val citaVm: CitaViewModel = viewModel(factory = ViewModelFactory(citaRepo))
            val mascotaVm: MascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo))
            CitaFormScreen(citaId = id, citaViewModel = citaVm, mascotaViewModel = mascotaVm, onBack = { navController.popBackStack() })
        }

        // 5. SERVICIOS
        composable(Screen.Servicios.route) {
            val viewModel: BreedsViewModel = viewModel(factory = ViewModelFactory(breedsRepo))
            ServiciosScreen(
                viewModel = viewModel,
                onNavigateToForm = { navController.navigate(Screen.ServicioForm.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ServicioForm.route) {
            val sViewModel: ServicioViewModel = viewModel(factory = ViewModelFactory(servicioRepo))
            val mViewModel: MascotaViewModel = viewModel(factory = ViewModelFactory(mascotaRepo))
            ServicioFormScreen(viewModel = sViewModel, mascotaViewModel = mViewModel, onBack = { navController.popBackStack() })
        }

        // 6. ASISTENTE (API RETROFIT / CHAT)
        composable(Screen.ConsultasRapidas.route) {
            ConsultasRapidasScreen(onBack = { navController.popBackStack() })
        }
    }
}
