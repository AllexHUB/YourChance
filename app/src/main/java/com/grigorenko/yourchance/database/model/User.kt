package com.grigorenko.yourchance.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val startupID: List<String>,
    val icon: Image,
    val type: String) : Parcelable {
    constructor() : this("", "", "", listOf(), Image(), "Startuper")
}