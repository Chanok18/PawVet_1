package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.dao.CitaDao
import com.example.pawvet_1.data.model.Cita
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await

/**
 * REPOSITORY CITA: Gestiona la sincronización de citas entre Room y Firestore.
 */
class CitaRepository(
    private val citaDao: CitaDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val userId: String get() = auth.currentUser?.uid ?: ""

    // CORREGIDO: No recibe parámetros, usa el userId interno del repositorio
    fun getCitasByUser(): Flow<List<Cita>> {
        val uid = userId
        return if (uid.isNotEmpty()) citaDao.getCitasByUser(uid) else emptyFlow()
    }

    suspend fun getCitaById(id: Int): Cita? = citaDao.getCitaById(id)

    suspend fun insertCita(cita: Cita) {
        val uid = userId
        if (uid.isEmpty()) return
        val docRef = firestore.collection("usuarios").document(uid).collection("citas").document()
        val citaConIds = cita.copy(usuarioId = uid, idFirestore = docRef.id)
        docRef.set(citaConIds).await()
        citaDao.insertCita(citaConIds)
    }

    suspend fun updateCita(cita: Cita) {
        val uid = userId
        if (uid.isEmpty() || cita.idFirestore.isEmpty()) return
        firestore.collection("usuarios").document(uid).collection("citas").document(cita.idFirestore).set(cita).await()
        citaDao.updateCita(cita)
    }

    suspend fun deleteCita(cita: Cita) {
        val uid = userId
        if (uid.isEmpty() || cita.idFirestore.isEmpty()) return
        firestore.collection("usuarios").document(uid).collection("citas").document(cita.idFirestore).delete().await()
        citaDao.deleteCita(cita)
    }

    suspend fun sincronizarDesdeFirestore() {
        val uid = userId
        if (uid.isEmpty()) return

        val snapshot = firestore
            .collection("usuarios")
            .document(uid)
            .collection("citas")
            .get()
            .await()

        val listaFirestore = snapshot.toObjects(Cita::class.java)

        citaDao.deleteCitasByUser(uid)

        for (cita in listaFirestore) {
            citaDao.insertCita(cita)
        }
    }
}
