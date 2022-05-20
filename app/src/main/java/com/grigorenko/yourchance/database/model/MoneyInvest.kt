package com.grigorenko.yourchance.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoneyInvest(
    val wholeSum: Long,
    val collectedSum: Long
) : Parcelable {
    constructor() : this(0, 0)
}