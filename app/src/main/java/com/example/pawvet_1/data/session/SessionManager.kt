package com.example.pawvet_1.data.session

import com.example.pawvet_1.data.firebase.FcmTokenSyncManager
import com.example.pawvet_1.data.firebase.UserProfileRemoteDataSource
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SessionState(
    val isLoggedIn: Boolean = false,
    val uid: String = "",
    val userName: String = "",
    val userEmail: String = ""
)

data class AuthResult(
    val success: Boolean,
    val message: String
)

class SessionManager(
    private val auth: FirebaseAuth,
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource,
    private val fcmTokenSyncManager: FcmTokenSyncManager
) {
    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()
    private val sessionScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun restoreSession() {
        val user = auth.currentUser
        if (user == null) {
            _sessionState.value = SessionState()
            return
        }
        _sessionState.value = buildSessionState(user.uid, user.email.orEmpty())
        runCatching { fcmTokenSyncManager.syncCurrentTokenIfPossible() }
    }

    suspend fun register(name: String, email: String, password: String): AuthResult {
        val cleanName = name.trim()
        val cleanEmail = email.trim()
        val cleanPassword = password.trim()

        if (cleanName.isBlank() || cleanEmail.isBlank() || cleanPassword.isBlank()) {
            return AuthResult(false, "Completa todos los campos para crear tu cuenta.")
        }

        return runCatching {
            val result = auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword).await()
            val uid = result.user?.uid ?: error("No se pudo crear el usuario")
            userProfileRemoteDataSource.createOrUpdateProfile(uid, cleanName, cleanEmail)
            _sessionState.value = SessionState(
                isLoggedIn = true,
                uid = uid,
                userName = cleanName,
                userEmail = cleanEmail
            )
            runCatching { fcmTokenSyncManager.syncCurrentTokenIfPossible() }
            AuthResult(true, "Cuenta creada correctamente.")
        }.getOrElse { AuthResult(false, mapAuthError(it)) }
    }

    suspend fun login(email: String, password: String): AuthResult {
        val cleanEmail = email.trim()
        val cleanPassword = password.trim()

        if (cleanEmail.isBlank() || cleanPassword.isBlank()) {
            return AuthResult(false, "Ingresa tu correo y contraseña.")
        }

        return runCatching {
            val result = auth.signInWithEmailAndPassword(cleanEmail, cleanPassword).await()
            val user = result.user ?: error("No se pudo iniciar sesión")
            _sessionState.value = buildSessionState(user.uid, user.email.orEmpty())
            runCatching { fcmTokenSyncManager.syncCurrentTokenIfPossible() }
            AuthResult(true, "Sesión iniciada.")
        }.getOrElse { AuthResult(false, mapAuthError(it)) }
    }

    fun logout() {
        val uid = _sessionState.value.uid.ifBlank { auth.currentUser?.uid.orEmpty() }
        auth.signOut()
        _sessionState.value = SessionState()
        if (uid.isNotBlank()) {
            sessionScope.launch {
                runCatching { userProfileRemoteDataSource.clearFcmToken(uid) }
            }
        }
    }

    private suspend fun buildSessionState(uid: String, fallbackEmail: String): SessionState {
        val profile = userProfileRemoteDataSource.getProfile(uid)
        return SessionState(
            isLoggedIn = true,
            uid = uid,
            userName = profile?.name.orEmpty().ifBlank { "Usuario PawVet" },
            userEmail = profile?.email.orEmpty().ifBlank { fallbackEmail }
        )
    }

    private fun mapAuthError(throwable: Throwable): String {
        if (throwable is FirebaseNetworkException) {
            return "No hay conexión a internet o Firebase no pudo responder. Verifica tu red."
        }

        val code = (throwable as? FirebaseAuthException)?.errorCode.orEmpty()
        return when (code) {
            "ERROR_INVALID_EMAIL" -> "El correo ingresado no es válido."
            "ERROR_INVALID_CREDENTIAL" -> "Las credenciales no son válidas."
            "ERROR_WRONG_PASSWORD" -> "La contraseña es incorrecta."
            "ERROR_USER_NOT_FOUND" -> "No existe una cuenta con ese correo."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Ese correo ya está registrado."
            "ERROR_WEAK_PASSWORD" -> "La contraseña debe tener al menos 6 caracteres."
            "ERROR_NETWORK_REQUEST_FAILED" -> "No hay conexión a internet."
            else -> throwable.message ?: "Ocurrió un error de autenticación."
        }
    }
}
