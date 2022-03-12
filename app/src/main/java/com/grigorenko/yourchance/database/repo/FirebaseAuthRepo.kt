package com.grigorenko.yourchance.database.repo

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthRepo {
    companion object {
        private val firebaseAuth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }
    }

    fun isStartuperCreatedWithEmail(email: String, password: String): Boolean {
        var response = false
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    response = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                }
            }
        return response
    }

    fun isStartuperAuthWithGoogleAcc(idToken: String): Boolean {
        var response = false
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "authStartuperWithGoogle:success")
                    response = true
                } else {
                    Log.w(ContentValues.TAG, "authStartuperWithGoogle:failure", task.exception)
                }
            }
        return response
    }

    fun isStartuperAuthWithEmail(email: String, password: String): Boolean {
        var response = false
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    response = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                }
            }
        return response
    }

    fun getStartuper(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun userSignOut() {
        firebaseAuth.signOut()
    }
}