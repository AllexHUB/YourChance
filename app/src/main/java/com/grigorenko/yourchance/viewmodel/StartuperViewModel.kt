package com.grigorenko.yourchance.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.grigorenko.yourchance.database.repo.FirebaseAuthRepo
import com.grigorenko.yourchance.database.repo.FirestoreRepo

class StartuperViewModel(): ViewModel() {
    private val firebaseAuthRepo = FirebaseAuthRepo()
    private val firestoreRepo = FirestoreRepo()

    fun isStartuperCreatedWithEmail(email: String, password: String): Boolean {
        return firebaseAuthRepo.isStartuperCreatedWithEmail(email, password)
    }

    fun isStartuperAuthWithGoogleAcc(idToken: String): Boolean {
        return firebaseAuthRepo.isStartuperAuthWithGoogleAcc(idToken)
    }

    fun isStartuperAuthWithEmail(email: String, password: String): Boolean {
        return firebaseAuthRepo.isStartuperAuthWithEmail(email, password)
    }

    fun getStartuper(): FirebaseUser? {
        return firebaseAuthRepo.getStartuper()
    }

    fun addNewStartuper(startuperUID: String) {
        firestoreRepo.addNewStartuper(startuperUID)
    }

    fun userSignOut() {
        firebaseAuthRepo.userSignOut()
    }
}