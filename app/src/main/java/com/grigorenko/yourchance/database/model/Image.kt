package com.grigorenko.yourchance.database.model

class Image(
    val name: String,
    var uri: String
) {
    constructor() : this("", "")
}