package com.grigorenko.yourchance.database.model

data class ChatPresentation(
    val companion: User,
    val firstUserUID: String,
    val secondUserUID: String,
    val lastMessage: Message
)