package com.grigorenko.yourchance.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val name: String,
    var uri: String
) : Parcelable {
    constructor() : this("", "")
}