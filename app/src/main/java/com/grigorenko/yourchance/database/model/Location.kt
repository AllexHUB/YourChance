package com.grigorenko.yourchance.database.model

data class Location(
    val country: String,
    val city: String
) {
    constructor() : this("", "")
}
