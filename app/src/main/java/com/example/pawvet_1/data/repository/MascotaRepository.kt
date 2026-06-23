package com.example.pawvet_1.data.repository

import com.example.pawvet_1.data.dao.MascotaDao
import com.example.pawvet_1.data.model.Mascota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import android.util.Log

class MascotaRepository(
    private val mascotaDao: MascotaDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    // Obtenemos el UID directamente de la sesión activa
    private val userId: String get() = auth.currentUser?.uid ?: ""

    // REPARADO: No pide parámetros, usa el UID de la sesión actual
    fun getMascotasByUser(): Flow<List<Mascota>> {
        val uid = userId
        return if (uid.isNotEmpty()) mascotaDao.getMascotasByUser(uid) else emptyFlow()
    }

    suspend fun getMascotaById(id: Int): Mascota? = mascotaDao.getMascotaById(id)

    suspend fun insertMascota(mascota: Mascota) {

        val uid = userId

        Log.d("PAWVET", "UID = $uid")

        if (uid.isEmpty()) {
            Log.d("PAWVET", "UID VACIO")
            return
        }

        val docRef = firestore
            .collection("usuarios")
            .document(uid)
            .collection("mascotas")
            .document()

        Log.d("PAWVET", "Guardando mascota...")

        val mascotaConIds = mascota.copy(
            usuarioId = uid,
            idFirestore = docRef.id
        )

        docRef.set(mascotaConIds).await()

        Log.d("PAWVET", "Mascota guardada en Firestore")

        mascotaDao.insertMascota(mascotaConIds)
    }

    suspend fun updateMascota(mascota: Mascota) {
        val uid = userId
        if (uid.isEmpty() || mascota.idFirestore.isEmpty()) return
        firestore.collection("usuarios").document(uid).collection("mascotas").document(mascota.idFirestore).set(mascota).await()
        mascotaDao.updateMascota(mascota)
    }

    suspend fun deleteMascota(mascota: Mascota) {
        val uid = userId
        if (uid.isEmpty() || mascota.idFirestore.isEmpty()) return
        firestore.collection("usuarios").document(uid).collection("mascotas").document(mascota.idFirestore).delete().await()
        mascotaDao.deleteMascota(mascota)
    }

    suspend fun sincronizarDesdeFirestore() {

        val uid = userId

        if (uid.isEmpty()) return

        val snapshot = firestore
            .collection("usuarios")
            .document(uid)
            .collection("mascotas")
            .get()
            .await()

        val listaFirestore = snapshot.toObjects(Mascota::class.java)

        mascotaDao.deleteMascotasByUser(uid)

        for (mascota in listaFirestore) {
            mascotaDao.insertMascota(mascota)
        }
    }

}
