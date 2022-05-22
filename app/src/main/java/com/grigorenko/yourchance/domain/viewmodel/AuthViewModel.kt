package com.grigorenko.yourchance.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.grigorenko.yourchance.domain.repo.FirebaseAuthRepo

class AuthViewModel: ViewModel() {
    private val firebaseAuthRepo = FirebaseAuthRepo()

    val firebaseUser: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>()
    }

    fun signUpWithEmail(email: String, password: String) {
        firebaseAuthRepo.signUpWithEmail(email, password, firebaseUser)
    }

    fun authWithGoogle(idToken: String) {
        firebaseAuthRepo.authWithGoogle(idToken, firebaseUser)
    }

    fun authWithEmail(email: String, password: String) {
        firebaseAuthRepo.authWithEmail(email, password, firebaseUser)
    }

    fun checkedForSignedUser(): FirebaseUser? {
        return firebaseAuthRepo.checkedForSignedUser()
    }

    fun userSignOut() {
        firebaseAuthRepo.userSignOut()
    }
}