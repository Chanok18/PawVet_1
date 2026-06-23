package com.example.pawvet_1.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * REPOSITORY DE AUTENTICACIÓN: Maneja la comunicación con Firebase Authentication.
 */
class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    // Obtener el usuario actual
    val usuarioActual: FirebaseUser?
        get() = firebaseAuth.currentUser

    // Registro con correo y contraseña
    suspend fun registrarUsuario(correo: String, contrasena: String): FirebaseUser? {
        return try {
            val resultado = firebaseAuth.createUserWithEmailAndPassword(correo, contrasena).await()
            resultado.user
        } catch (e: Exception) {
            throw e
        }
    }

    // Login con correo y contraseña
    suspend fun iniciarSesion(correo: String, contrasena: String): FirebaseUser? {
        return try {
            val resultado = firebaseAuth.signInWithEmailAndPassword(correo, contrasena).await()
            resultado.user
        } catch (e: Exception) {
            throw e
        }
    }

    // Cerrar sesión
    fun cerrarSesion() {
        firebaseAuth.signOut()
    }
}
