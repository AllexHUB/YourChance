package com.grigorenko.yourchance.database.repo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.database.model.User
import java.io.ByteArrayOutputStream

class FirestoreRepo {
    companion object {
        private val firestoreInstance: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }
        private val storage: FirebaseStorage by lazy {
            Firebase.storage
        }
        private val storageRef = storage.reference
    }

    private val userRef = firestoreInstance.collection("users")
    private val startupRef = firestoreInstance.collection("startups")

    fun addNewUser(userUID: String, user: User) {
        userRef.document(userUID)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("USER", "Adding new user in Firestore successfully")
            }
            .addOnFailureListener {
                Log.e("USER", "Error adding new user ")
            }
    }

    fun getUserByUID(userUID: String, retrievedUser: MutableLiveData<User>) {
        userRef.document(userUID)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    retrievedUser.postValue(null)
                    Log.e("USER", "Get user failed with ", error)
                }

                if (value != null) {
                    retrievedUser.postValue(value.toObject<User>())
                    Log.d("USER", "User received")
                }
            }
    }

    fun addNewStartupToFirestore(imgDrawable: Drawable, startup: Startup, userUID: String) {
        uploadImage(imgDrawable, startup.image.name).addOnSuccessListener { uri ->
            startup.image.uri = uri.toString()
            startupRef.document()
                .set(startup)
                .addOnSuccessListener {
                    Log.d("STARTUP", "Adding new startup in Firestore successfully")
                    getStartupIdByImage(startup.image).addOnSuccessListener {
                        for (document in it)
                            addNewStartupToUser(userUID, document.id)
                    }
                }
                .addOnFailureListener {
                    Log.e("STARTUP", "Error adding new startup ")
                }
        }
    }

    fun manageUserStartups(userUID: String, userStartups: MutableLiveData<List<Startup>>) {
        userRef.document(userUID)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("USER", "Listen failed.", error)
                    return@addSnapshotListener
                }

                if (value?.get("startupID") != null && value.get("startupID").toString() != "[]") {
                    val user = value.toObject<User>()!!
                    getUserStartups(user.startupID, userStartups)
                } else {
                    Log.d("STARTUP", "Current data: null")
                    userStartups.postValue(null)
                }
            }
    }

    private fun getUserStartups(
        listOfStartupId: List<String>,
        userStartups: MutableLiveData<List<Startup>>
    ) {
        val list = mutableListOf<Startup>()
        for (startupId in listOfStartupId) {
            startupRef.document(startupId)
                .get()
                .addOnSuccessListener { document ->
                    list.add(document.toObject<Startup>()!!)
                }
                .addOnCompleteListener {
                    userStartups.postValue(list)
                }
        }
    }

    fun deleteUserStartup(userUID: String, startup: Startup) {
        getStartupIdByImage(startup.image).addOnSuccessListener {
            for (document in it) {
                userRef.document(userUID)
                    .update("startupID", FieldValue.arrayRemove(document.id))
                    .addOnSuccessListener {
                        startupRef.document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("STARTUP", "Startup with id ${document.id} was deleted")
                                storageRef.child(startup.image.name).delete()
                            }.addOnFailureListener {
                                Log.e("STARTUP", "Deletion error of startup with id ${document.id}")
                            }
                    }
            }
        }
    }

    fun updateUserStartup(
        imgDrawable: Drawable?,
        startup: Startup,
        oldImage: Image,
        isStartupUpdated: MutableLiveData<Boolean>
    ) {
        getStartupIdByImage(oldImage).addOnSuccessListener {
            for (document in it) {
                if (imgDrawable != null) {
                    uploadImage(imgDrawable, startup.image.name).addOnSuccessListener { uri ->
                        startup.image.uri = uri.toString()
                        updateStartupDetails(document.id, startup, isStartupUpdated)
                    }
                } else
                    updateStartupDetails(document.id, startup, isStartupUpdated)
            }
        }

    }

    fun getStartupsOrderedByStars(sortedStartupsList: MutableLiveData<List<Startup>>) {
        val listOfStartups = mutableListOf<Startup>()
        startupRef
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("USER", "Listen failed.", error)
                    return@addSnapshotListener
                }

                for (startup in value!!.documents)
                    listOfStartups.add(startup.toObject<Startup>()!!)

                sortedStartupsList.postValue(listOfStartups)
            }
    }

    fun getStartupsOrderedByDate(sortedStartupsList: MutableLiveData<List<Startup>>) {
        val listOfStartups = mutableListOf<Startup>()
        startupRef
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("USER", "Listen failed.", error)
                    return@addSnapshotListener
                }

                for (startup in value!!.documents)
                    listOfStartups.add(startup.toObject<Startup>()!!)

                sortedStartupsList.postValue(listOfStartups)
            }
    }

    fun incrementStartupViews(image: Image) {
        getStartupIdByImage(image).addOnSuccessListener {
            for (document in it)
                startupRef.document(document.id)
                    .update("views", FieldValue.increment(1))
        }
    }

    fun updateUserIcon(userUID: String, icon: Image, iconDrawable: Drawable) {
        uploadImage(iconDrawable, icon.name).addOnSuccessListener {
            icon.uri = it.toString()
            userRef.document(userUID)
                .update("icon", icon)
        }
    }

    private fun addNewStartupToUser(userUid: String, startupId: String) {
        firestoreInstance.collection("users").document(userUid)
            .update("startupID", FieldValue.arrayUnion(startupId))
    }

    private fun updateStartupDetails(
        startupId: String,
        startup: Startup,
        isStartupUpdated: MutableLiveData<Boolean>
    ) {
        firestoreInstance.collection("startups")
            .document(startupId)
            .set(startup)
            .addOnCompleteListener {
                isStartupUpdated.postValue(true)
            }.addOnFailureListener {
                isStartupUpdated.postValue(false)
            }
    }

    private fun uploadImage(imgDrawable: Drawable?, name: String): Task<Uri> {
        val imageRef = storageRef.child(name)

        val bitmap = (imgDrawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)

        return uploadTask.continueWithTask {
            imageRef.downloadUrl
        }.addOnCompleteListener {
            Log.d("IMAGE", "Image was uploaded")
        }.addOnFailureListener { exception ->
            Log.e("IMAGE", "Get failed with ", exception)
        }
    }

    private fun getStartupIdByImage(image: Image): Task<QuerySnapshot> {
        return firestoreInstance.collection("startups")
            .whereEqualTo("image", image)
            .get()
    }
}