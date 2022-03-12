package com.grigorenko.yourchance.database.repo

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepo {
    companion object {
        private val firestoreInstance: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }
    }

    fun addNewStartuper(startuperUID: String) {
        firestoreInstance.collection("startuper")
            .add(startuperUID)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Startuper added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding startuper", e)
            }
    }
}