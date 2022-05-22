package com.grigorenko.yourchance.ui.startuper.messages

import com.grigorenko.yourchance.domain.model.User

interface ChatClickListener {
    fun onClick(firstUserId: String, secondUserId: String, companion: User)
}