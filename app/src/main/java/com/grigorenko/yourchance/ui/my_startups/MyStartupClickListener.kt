package com.grigorenko.yourchance.ui.my_startups

import com.grigorenko.yourchance.database.model.Startup

interface MyStartupClickListener {
    fun onDeleteStartup(startup: Startup)
    fun onEditStartup(startup: Startup)
}