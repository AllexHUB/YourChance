package com.grigorenko.yourchance.domain.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Startup(
    val category: String,
    val description: String,
    val image: Image,
    val moneyInvest: MoneyInvest,
    val name: String,
    val location: Location,
    val date: Date,
    val views: Int,
    val ownerId: String
) : Parcelable {
    private companion object : Parceler<Startup> {
        override fun create(parcel: Parcel): Startup {
            return Startup()
        }

        override fun Startup.write(parcel: Parcel, flags: Int) {
        }
    }

    constructor() : this("", "", Image(), MoneyInvest(), "", Location(), Date(), 0, "")
}