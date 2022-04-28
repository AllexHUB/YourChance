package com.grigorenko.yourchance.ui.create_new_startup

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.model.Location
import com.grigorenko.yourchance.database.model.MoneyInvest
import com.grigorenko.yourchance.database.model.Startup

class StartupDetailsViewModel : ViewModel() {
    private lateinit var category: String
    private lateinit var name: String
    private lateinit var moneyInvest: MoneyInvest
    private lateinit var description: String
    private lateinit var image: Image
    private lateinit var location: Location
    private lateinit var date: Timestamp
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

    fun setDate(date: Timestamp) {
        this.date = date
    }

    fun setViews(views: Int) {
        this.views = views
    }

    fun makeNewStartup(): Startup {
        return Startup(category,
                        description,
                        image,
                        moneyInvest,
                        name,
                        location,
                        date,
                        views)
    }
}