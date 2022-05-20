package com.grigorenko.yourchance.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val country: String,
    val city: String
) : Parcelable {
    constructor() : this("", "")
}
