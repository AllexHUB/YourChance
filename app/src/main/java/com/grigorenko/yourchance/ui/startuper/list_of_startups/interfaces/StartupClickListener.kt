package com.grigorenko.yourchance.ui.startuper.list_of_startups.interfaces

import com.grigorenko.yourchance.database.model.Startup

interface StartupClickListener {
    fun onClick(startup: Startup, isFavoriteStartup: Boolean)
}