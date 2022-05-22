package com.grigorenko.yourchance.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val country: String,
    val city: String
) : Parcelable {
    constructor() : this("", "")
}
