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
import com.grigorenko.yourchance.database.model.*
import java.io.ByteArrayOutputStream

class FirestoreRepo {
    companion object {
        private val firestoreInstance: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }
        private val storage: FirebaseStorage by lazy {
            Firebase.storage
        }
    }

    private val userRef = firestoreInstance.collection("users")
    private val startupRef = firestoreInstance.collection("startups")
    private val chatRef = firestoreInstance.collection("chats")
    private val storageRef = storage.reference

    fun addNewUser(userUID: String, user: User, retrievedUser: MutableLiveData<User>) {
        userRef.document(userUID)
            .set(user)
            .addOnSuccessListener {
                retrievedUser.value = user
                Log.d("USER", "Adding new user in Firestore successfully")
            }
            .addOnFailureListener {
                retrievedUser.value = null
                Log.e("USER", "Error adding new user ")
            }
    }

    fun checkForUserExists(email: String, userExists: MutableLiveData<Boolean>) {
        userRef
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                if (it.documents.size != 0)
                    userExists.postValue(true)
                else
                    userExists.postValue(false)
            }
    }

    fun getUserByUID(userUID: String, retrievedUser: MutableLiveData<User>) {
        userRef.document(userUID)
            .get()
            .addOnSuccessListener {
                retrievedUser.value = it.toObject(User::class.java)
                Log.d("USER", "User received")
            }.addOnFailureListener {
                retrievedUser.value = null
            }
    }

    fun addNewStartupToFirestore(imgDrawable: Drawable, startup: Startup, userUID: String) {
        uploadImage(imgDrawable, startup.image.name).addOnSuccessListener { uri ->
            startup.image.uri = uri.toString()
            val newStartup = startupRef.document()
            newStartup
                .set(startup)
                .addOnSuccessListener {
                    addNewStartupToUser(userUID, newStartup.id)
                }
        }
    }

    fun addStartupToFavorites(userUID: String, startupImage: Image) {
        getStartupIdByImage(startupImage).addOnSuccessListener {
            for (document in it) {
                addNewStartupToUser(userUID, document.id)
            }
        }
    }

    fun deleteStartupFromFavorites(userUID: String, startup: Startup) {
        getStartupIdByImage(startup.image).addOnSuccessListener {
            for (document in it) {
                userRef.document(userUID)
                    .update("startupID", FieldValue.arrayRemove(document.id))
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

                val user = value?.toObject(User::class.java)!!
                if (!user.startupID.isNullOrEmpty()) {
                    getUserStartups(user.startupID, userStartups)
                } else {
                    userStartups.postValue(null)
                    Log.d("STARTUP", "Current data: null")
                }
            }
    }

    fun deleteUserStartup(startupImage: Image) {
        getStartupIdByImage(startupImage).addOnSuccessListener { snapshot ->
            for (document in snapshot) {
                removeStartupIdFromUsers(document.id).addOnCompleteListener {
                    startupRef.document(document.id)
                        .delete()
                    storageRef.child(startupImage.name)
                        .delete()
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

    fun addMessageToChat(userIds: List<String>, message: Message) {
        getOrCreateChat(userIds) {
            chatRef.document(it).collection("messages").document()
                .set(message)
        }
    }

    fun manageChatMessages(userIds: List<String>, messages: MutableLiveData<List<Message>>) {
        getOrCreateChat(userIds) {
            loadChatMessages(it, messages)
        }
    }

    fun getUserChats(
        userUID: String,
        userChatsPresentation: MutableLiveData<List<ChatPresentation>>
    ) {
        val chatPresentation = mutableListOf<ChatPresentation>()
        userRef.document(userUID).collection("engagedChats")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.documents.size != 0) {
                    for (document in snapshot.documents) {
                        userRef.document(document.id)
                            .get()
                            .addOnSuccessListener { userDocument ->
                                val companion = userDocument.toObject(User::class.java)!!
                                getExistingChat(document["chatId"].toString()) { chat ->
                                    val lastMessage = if (!chat.messages.isNullOrEmpty()) {
                                        chat.messages.last()
                                    } else {
                                        Message()
                                    }
                                    chatPresentation.add(
                                        ChatPresentation(
                                            companion,
                                            userUID,
                                            document.id,
                                            lastMessage
                                        )
                                    )
                                    userChatsPresentation.postValue(chatPresentation)
                                }
                            }
                    }
                }
            }
    }

    private fun getExistingChat(chatId: String, onComplete: (chat: Chat) -> Unit) {
        chatRef.document(chatId)
            .get()
            .addOnSuccessListener { document ->
                document.reference.collection("messages").get()
                    .addOnSuccessListener { messagesSnapshot ->
                        val tempChat = document.toObject(Chat::class.java)!!
                        val tempMessages = mutableListOf<Message>()
                        for (i in messagesSnapshot.documents)
                            tempMessages.add(i.toObject(Message::class.java)!!)
                        onComplete(Chat(tempChat.userIds, tempMessages))
                    }
            }
    }

    private fun loadChatMessages(chatId: String, messages: MutableLiveData<List<Message>>) {
        val listMessages = mutableListOf<Message>()
        chatRef.document(chatId).collection("messages")
            .orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                listMessages.clear()
                if (error != null) {
                    Log.w("CHAT", "Listen failed.", error)
                    return@addSnapshotListener
                }

                for (message in value!!.documents)
                    listMessages.add(message.toObject<Message>()!!)
                messages.postValue(listMessages)
            }
    }

    private fun getOrCreateChat(userIds: List<String>, onComplete: (channelId: String) -> Unit) {
        userRef.document(userIds[0])
            .get()
            .addOnSuccessListener { user ->
                user.reference.collection("engagedChats").document(userIds[1])
                    .get()
                    .addOnSuccessListener { taskDocument ->
                        if (taskDocument.data != null)
                            onComplete(taskDocument["chatId"].toString())
                        else {
                            val newChat = chatRef.document()
                            newChat.set(hashMapOf("userIds" to userIds))
                            userRef.document(userIds[0]).collection("engagedChats")
                                .document(userIds[1])
                                .set(hashMapOf("chatId" to newChat.id))
                            userRef.document(userIds[1]).collection("engagedChats")
                                .document(userIds[0])
                                .set(hashMapOf("chatId" to newChat.id))
                            onComplete(newChat.id)
                        }
                    }
            }
    }


    private fun addNewStartupToUser(userUid: String, startupId: String) {
        firestoreInstance.collection("users").document(userUid)
            .update("startupID", FieldValue.arrayUnion(startupId))
    }

    private fun removeStartupIdFromUsers(startupId: String): Task<QuerySnapshot> {
        return userRef
            .whereArrayContains("startupID", startupId)
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    userRef.document(document.id)
                        .update("startupID", FieldValue.arrayRemove(startupId))
                }
            }
    }

    private fun updateStartupDetails(
        startupId: String,
        startup: Startup,
        isStartupUpdated: MutableLiveData<Boolean>
    ) {
        startupRef
            .document(startupId)
            .set(startup, SetOptions.merge())
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
        return startupRef
            .whereEqualTo("image", image)
            .get()
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
                    list.add(document.toObject(Startup::class.java)!!)
                }
                .addOnCompleteListener {
                    userStartups.postValue(list)
                }
        }
    }
}