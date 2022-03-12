package com.grigorenko.yourchance.database.repo

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepo {
    companion object {
        val firestoreInstance: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }
    }

    // Create a new user with a first and last name
    private val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
    )

    // Add a new document with a generated ID
    fun start() {
        firestoreInstance.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
        }
    }
}