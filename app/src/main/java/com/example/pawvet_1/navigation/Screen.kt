package com.example.pawvet_1.navigation

/**
 * RUTAS DE NAVEGACIÓN: Definición de todos los destinos de la aplicación.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Perfil : Screen("perfil")
    
    // Mascotas
    object MascotaDetalle : Screen("mascota_detalle/{mascotaId}") {
        fun createRoute(id: Int) = "mascota_detalle/$id"
    }
    object MascotaForm : Screen("mascota_form/{mascotaId}") {
        fun createRoute(id: Int = 0) = "mascota_form/$id"
    }

    // Citas
    object CitaForm : Screen("cita_form/{citaId}") {
        fun createRoute(id: Int = 0) = "cita_form/$id"
    }

    // Servicios
    object Servicios : Screen("servicios")
    object ServicioForm : Screen("servicio_form")

    // API
    object ConsultasRapidas : Screen("consultas_rapidas")
}
