package com.grigorenko.yourchance.database.repo

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthRepo {
    companion object {
        private val firebaseAuth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }
    }

    fun signUpWithEmail(email: String, password: String, user: MutableLiveData<FirebaseUser>) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    user.postValue(firebaseAuth.currentUser)
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    user.postValue(null)
                }
            }
    }

    fun authWithGoogle(idToken: String, user: MutableLiveData<FirebaseUser>) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "authStartuperWithGoogle:success")
                    user.postValue(firebaseAuth.currentUser)
                } else {
                    Log.w(ContentValues.TAG, "authStartuperWithGoogle:failure", task.exception)
                    user.postValue(null)
                }
            }
    }

    fun authWithEmail(email: String, password: String, user: MutableLiveData<FirebaseUser>) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    user.postValue(firebaseAuth.currentUser)
                } else {
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    user.postValue(null)
                }
            }
    }

    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun userSignOut() {
        firebaseAuth.signOut()
    }
}