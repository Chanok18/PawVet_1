package com.example.pawvet_1.ui.viewmodel

import com.google.firebase.auth.FirebaseUser

/**
 * ESTADO DE AUTENTICACIÓN: Define los posibles estados de la sesión del usuario.
 */
data class AuthUiState(
    val usuario: FirebaseUser? = null,
    val estaCargando: Boolean = false,
    val mensajeError: String? = null,
    val registroExitoso: Boolean = false
)
