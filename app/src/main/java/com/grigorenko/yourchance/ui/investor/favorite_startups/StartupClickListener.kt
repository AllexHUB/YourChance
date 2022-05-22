package com.grigorenko.yourchance.ui.investor.favorite_startups

import com.grigorenko.yourchance.domain.model.Startup

interface StartupClickListener {
    fun onClick(startup: Startup, isFavoriteStartup: Boolean)
}