package com.grigorenko.yourchance.database.model

class MoneyInvest(
    val wholeSum: Long,
    val collectedSum: Long
) {
    constructor() : this(0, 0)
}