package com.grigorenko.yourchance.database

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {
    private val db = Firebase.firestore

    // Create a new user with a first and last name
    private val user = hashMapOf(
        "first" to "Ada",
        "last" to "Lovelace",
        "born" to 1815
    )

    // Add a new document with a generated ID
    fun start() {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}