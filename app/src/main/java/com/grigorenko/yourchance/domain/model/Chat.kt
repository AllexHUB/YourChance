package com.grigorenko.yourchance.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val userIds: List<String>,
    val messages: List<Message>
) : Parcelable {
    constructor() : this(listOf(), listOf())
}
