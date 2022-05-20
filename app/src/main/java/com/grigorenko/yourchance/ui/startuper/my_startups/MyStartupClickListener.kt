package com.grigorenko.yourchance.ui.startuper.my_startups

import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.model.Startup

interface MyStartupClickListener {
    fun onDeleteStartup(startupImage: Image)
    fun onEditStartup(startup: Startup)
}