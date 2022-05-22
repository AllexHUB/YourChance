package com.grigorenko.yourchance.domain.model

data class ChatPresentation(
    val companion: User,
    val firstUserUID: String,
    val secondUserUID: String,
    val lastMessage: Message
)