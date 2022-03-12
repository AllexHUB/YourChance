package com.grigorenko.yourchance.database.repo

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthRepo {
    companion object {
        val firebaseAuth: FirebaseAuth by lazy {
            FirebaseAuth.getInstance()
        }
    }
}