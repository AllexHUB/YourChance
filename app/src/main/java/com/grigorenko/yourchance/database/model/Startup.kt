package com.grigorenko.yourchance.database.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Startup(
    val category: String,
    val description: String,
    val image: Image,
    val moneyInvest: MoneyInvest,
    val name: String,
    val location: Location,
    val date: Timestamp,
    val views: Int
    ) : Parcelable {
    private companion object : Parceler<Startup> {
        override fun create(parcel: Parcel): Startup {
            return Startup()
        }
        override fun Startup.write(parcel: Parcel, flags: Int) {
        }
    }
    constructor() : this("", "", Image(), MoneyInvest(), "", Location(), Timestamp.now(), 0)
}