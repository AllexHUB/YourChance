package com.grigorenko.yourchance.database.model

class User(
    val email: String,
    val fullName: String,
    val phoneNumber: String,
    val startupID: List<String>,
    val icon: Image) {
    constructor() : this("", "", "", listOf(), Image())
}