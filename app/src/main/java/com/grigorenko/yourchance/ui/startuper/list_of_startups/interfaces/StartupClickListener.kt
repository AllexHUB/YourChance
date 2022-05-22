package com.grigorenko.yourchance.ui.startuper.list_of_startups.interfaces

import com.grigorenko.yourchance.domain.model.Startup

interface StartupClickListener {
    fun onClick(startup: Startup, isFavoriteStartup: Boolean, isOwnStartup: Boolean, isUserStartuper: Boolean)
}