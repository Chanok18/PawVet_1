package com.example.pawvet_1.navigation

/**
 * RUTAS ESENCIALES PARA LA SUSTENTACIÓN.
 * Solo 4 flujos principales para cumplir con la rúbrica de forma simple.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Perfil : Screen("perfil") // Aquí mostramos la lista (CRUD)
    
    // Formulario de Mascota (Para Crear/Editar)
    object MascotaForm : Screen("mascota_form/{mascotaId}") {
        fun createRoute(id: Int = 0) = "mascota_form/$id"
    }

    // Pantalla de la API (Consumo de datos externos)
    object ConsultasRapidas : Screen("consultas_rapidas")
}
