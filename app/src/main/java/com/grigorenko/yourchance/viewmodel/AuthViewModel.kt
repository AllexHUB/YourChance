package com.grigorenko.yourchance.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.grigorenko.yourchance.database.repo.FirebaseAuthRepo
import com.grigorenko.yourchance.database.repo.FirestoreRepo

class AuthViewModel(): ViewModel() {
    private val firebaseAuthRepo = FirebaseAuthRepo()
    private val firestoreRepo = FirestoreRepo()

    val user: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>()
    }

    fun signUpWithEmail(email: String, password: String) {
        firebaseAuthRepo.signUpWithEmail(email, password, user)
    }

    fun authWithGoogle(idToken: String) {
        firebaseAuthRepo.authWithGoogle(idToken, user)
    }

    fun authWithEmail(email: String, password: String) {
        firebaseAuthRepo.authWithEmail(email, password, user)
    }

    fun getUser(): FirebaseUser? {
        return firebaseAuthRepo.getUser()
    }

    fun addNewUser(user: FirebaseUser, name: String, phoneNumber: String) {
        firestoreRepo.addNewUser(user, name, phoneNumber)
    }

    fun userSignOut() {
        firebaseAuthRepo.userSignOut()
    }
}