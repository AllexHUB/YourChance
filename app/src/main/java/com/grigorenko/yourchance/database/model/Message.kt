package com.grigorenko.yourchance.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Message(
    val id: String,
    val senderId: String,
    val text: String,
    val date: Date
) : Parcelable {
    constructor() : this("", "", "", Date())
}