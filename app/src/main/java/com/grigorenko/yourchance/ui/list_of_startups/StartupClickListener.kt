package com.grigorenko.yourchance.ui.list_of_startups

import com.grigorenko.yourchance.database.model.Startup

interface StartupClickListener {
    fun onClick(startup: Startup)
}