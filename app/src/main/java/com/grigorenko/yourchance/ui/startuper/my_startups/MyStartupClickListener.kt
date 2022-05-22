package com.grigorenko.yourchance.ui.startuper.my_startups

import com.grigorenko.yourchance.domain.model.Image
import com.grigorenko.yourchance.domain.model.Startup

interface MyStartupClickListener {
    fun onDeleteStartup(startupImage: Image)
    fun onEditStartup(startup: Startup)
}