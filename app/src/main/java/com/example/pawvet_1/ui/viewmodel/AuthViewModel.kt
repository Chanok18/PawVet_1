package com.example.pawvet_1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawvet_1.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * VIEWMODEL DE AUTENTICACIÓN: Gestiona el estado de la sesión y las operaciones de Login/Registro.
 */
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // Verificar si hay una sesión activa al iniciar
        verificarSesion()
    }

    private fun verificarSesion() {
        val usuario = authRepository.usuarioActual
        _uiState.update { it.copy(usuario = usuario) }
    }

    fun registrar(correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true, mensajeError = null) }
            try {
                val usuario = authRepository.registrarUsuario(correo, contrasena)
                _uiState.update { it.copy(usuario = usuario, registroExitoso = true, estaCargando = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = e.localizedMessage, estaCargando = false) }
            }
        }
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true, mensajeError = null) }
            try {
                val usuario = authRepository.iniciarSesion(correo, contrasena)
                _uiState.update { it.copy(usuario = usuario, estaCargando = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = e.localizedMessage, estaCargando = false) }
            }
        }
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        _uiState.update { it.copy(usuario = null, registroExitoso = false) }
    }
}
