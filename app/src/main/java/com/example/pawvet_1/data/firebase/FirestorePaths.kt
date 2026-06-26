package com.example.pawvet_1.data.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

private const val USERS_COLLECTION = "users"
private const val MASCOTAS_COLLECTION = "mascotas"
private const val CITAS_COLLECTION = "citas"
private const val SERVICIOS_COLLECTION = "servicios"

fun FirebaseFirestore.userDocument(uid: String): DocumentReference =
    collection(USERS_COLLECTION).document(uid)

fun FirebaseFirestore.userMascotasCollection(uid: String): CollectionReference =
    userDocument(uid).collection(MASCOTAS_COLLECTION)

fun FirebaseFirestore.userCitasCollection(uid: String): CollectionReference =
    userDocument(uid).collection(CITAS_COLLECTION)

fun FirebaseFirestore.userServiciosCollection(uid: String): CollectionReference =
    userDocument(uid).collection(SERVICIOS_COLLECTION)
