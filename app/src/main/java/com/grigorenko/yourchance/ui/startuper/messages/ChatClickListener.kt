package com.grigorenko.yourchance.ui.startuper.messages

import com.grigorenko.yourchance.database.model.User

interface ChatClickListener {
    fun onClick(firstUserId: String, secondUserId: String, companion: User)
}