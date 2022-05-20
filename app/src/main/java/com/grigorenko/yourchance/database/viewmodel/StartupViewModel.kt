package com.grigorenko.yourchance.database.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.database.repo.FirestoreRepo

class StartupViewModel: ViewModel() {
    private val firestoreRepo = FirestoreRepo()

    val userStartups = MutableLiveData<List<Startup>>()
    val latestStartups = MutableLiveData<List<Startup>>()
    val popularStartups = MutableLiveData<List<Startup>>()
    val isStartupUpdated = MutableLiveData<Boolean>()

    fun addNewStartupToFirestore(imgDrawable: Drawable, startup: Startup, userUID: String) {
        firestoreRepo.addNewStartupToFirestore(imgDrawable, startup, userUID)
    }

    fun manageUserStartups(userUID: String) {
        firestoreRepo.manageUserStartups(userUID, userStartups)
    }

    fun deleteUserStartup(startupImage: Image) {
        firestoreRepo.deleteUserStartup(startupImage)
    }

    fun updateUserStartup(imgDrawable: Drawable?, startup: Startup, oldImage: Image) {
        firestoreRepo.updateUserStartup(imgDrawable, startup, oldImage, isStartupUpdated)
    }

    fun getStartupsOrderedByStars() {
        firestoreRepo.getStartupsOrderedByStars(popularStartups)
    }

    fun getStartupsOrderedByDate() {
        firestoreRepo.getStartupsOrderedByDate(latestStartups)
    }

    fun addStartupToFavorites(userUID: String, startupImage: Image) {
        firestoreRepo.addStartupToFavorites(userUID, startupImage)
    }

    fun deleteStartupFromFavorites(userUID: String, startup: Startup) {
        firestoreRepo.deleteStartupFromFavorites(userUID, startup)
    }

    fun incrementStartupViews(image: Image) {
        firestoreRepo.incrementStartupViews(image)
    }
}