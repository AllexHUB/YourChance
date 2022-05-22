package com.grigorenko.yourchance.domain.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grigorenko.yourchance.domain.model.Image
import com.grigorenko.yourchance.domain.model.User
import com.grigorenko.yourchance.domain.repo.FirebaseAuthRepo
import com.grigorenko.yourchance.domain.repo.FirestoreRepo

class UserViewModel : ViewModel() {
    private val firestoreRepo = FirestoreRepo()
    private val firebaseAuthRepo = FirebaseAuthRepo()

    val userExists = MutableLiveData<Boolean>()

    val userModel = MutableLiveData<User>()

    fun getUserByUID(userUID: String) {
        firestoreRepo.getUserByUID(userUID, userModel)
    }

    fun addNewUser(userUID: String, user: User) {
        firestoreRepo.addNewUser(userUID, user, userModel)
    }

    fun getCurrentUserUID(): String {
        return firebaseAuthRepo.getCurrentUserUID()
    }

    fun updateUserIcon(userUID: String, icon: Image, iconDrawable: Drawable) {
        firestoreRepo.updateUserIcon(userUID, icon, iconDrawable)
    }

    fun checkForUserExists(email: String) {
        firestoreRepo.checkForUserExists(email, userExists)
    }
}