package com.grigorenko.yourchance.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grigorenko.yourchance.domain.model.ChatPresentation
import com.grigorenko.yourchance.domain.model.Message
import com.grigorenko.yourchance.domain.repo.FirestoreRepo

class ChatViewModel : ViewModel() {
    private val firestoreRepo = FirestoreRepo()

    val userChatsPresentation = MutableLiveData<List<ChatPresentation>>()

    val messages = MutableLiveData<List<Message>>()

//    private val _messages = MutableLiveData<List<Message>>()
//    val messages: LiveData<List<Message>> get() = _messages

    fun manageChatMessages(userIds: List<String>) {
        firestoreRepo.manageChatMessages(userIds, messages)
    }

    fun addMessageToChat(userIds: List<String>, message: Message) {
        firestoreRepo.addMessageToChat(userIds, message)
    }

    fun getUserChats(userUID: String) {
        firestoreRepo.getUserChats(userUID, userChatsPresentation)
    }
}