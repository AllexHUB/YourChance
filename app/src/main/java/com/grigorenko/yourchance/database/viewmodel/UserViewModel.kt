package com.grigorenko.yourchance.database.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.model.User
import com.grigorenko.yourchance.database.repo.FirebaseAuthRepo
import com.grigorenko.yourchance.database.repo.FirestoreRepo

class UserViewModel: ViewModel() {
    private val firestoreRepo = FirestoreRepo()
    private val firebaseAuthRepo = FirebaseAuthRepo()

    val userModel: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun getUserByUID(userUID: String) {
        firestoreRepo.getUserByUID(userUID, userModel)
    }

    fun addNewUser(userUID: String, user: User) {
        firestoreRepo.addNewUser(userUID, user)
    }

    fun getCurrentUserUID(): String {
        return firebaseAuthRepo.getCurrentUserUID()
    }

    fun updateUserIcon(userUID: String, icon: Image, iconDrawable: Drawable) {
        firestoreRepo.updateUserIcon(userUID, icon, iconDrawable)
    }
}