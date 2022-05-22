package com.grigorenko.yourchance.ui.startuper.create_new_startup

import androidx.lifecycle.ViewModel
import com.grigorenko.yourchance.domain.model.Image
import com.grigorenko.yourchance.domain.model.Location
import com.grigorenko.yourchance.domain.model.MoneyInvest
import com.grigorenko.yourchance.domain.model.Startup
import java.util.*

class StartupDetailsViewModel : ViewModel() {
    private lateinit var category: String
    private lateinit var name: String
    private lateinit var moneyInvest: MoneyInvest
    private lateinit var description: String
    private lateinit var image: Image
    private lateinit var location: Location
    private lateinit var date: Date
    private lateinit var ownerId: String
    private var views: Int = 0

    fun setCategory(category: String) {
        this.category = category
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setMoneyInvest(moneyInvest: MoneyInvest) {
        this.moneyInvest = moneyInvest
    }

    fun setImage(image: Image) {
        this.image = image
    }

    fun setLocation(location: Location) {
        this.location = location
    }

    fun setDate(date: Date) {
        this.date = date
    }

    fun setViews(views: Int) {
        this.views = views
    }

    fun setOwnerId(ownerId: String) {
        this.ownerId = ownerId
    }

    fun makeNewStartup(): Startup {
        return Startup(
            category,
            description,
            image,
            moneyInvest,
            name,
            location,
            date,
            views,
            ownerId
        )
    }
}