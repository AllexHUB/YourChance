package com.grigorenko.yourchance.database.repo

import android.nfc.Tag
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.grigorenko.yourchance.database.model.User

class FirestoreRepo {
    companion object {
        private val firestoreInstance: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }
    }

    fun addNewUser(firebaseUser: FirebaseUser, name: String, phoneNumber: String) {
        val user = User(
            firebaseUser.email!!,
            name,
            phoneNumber
        )
        firestoreInstance.collection("users")
            .document(firebaseUser.uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("USER", "Adding new user in Firestore successfully")
            }
            .addOnFailureListener {
                Log.e("USER", "Error adding new user ")
            }
    }
}