package com.grigorenko.yourchance.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.database.model.User
import com.grigorenko.yourchance.database.viewmodel.StartupViewModel
import com.grigorenko.yourchance.database.viewmodel.UserViewModel
import com.grigorenko.yourchance.databinding.ActivityMainBinding
import com.grigorenko.yourchance.ui.startuper.list_of_startups.interfaces.NavigationDrawerDisabler
import com.grigorenko.yourchance.ui.startuper.user_profile.UserProfileActivity
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), NavigationDrawerDisabler {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var userViewModel: UserViewModel
    private lateinit var startupViewModel: StartupViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        startupViewModel = ViewModelProvider(this)[StartupViewModel::class.java]
        val user = intent.getParcelableExtra<User>("user")

        val userUID = userViewModel.getCurrentUserUID()
        startupViewModel.manageUserStartups(userUID)

        toolbar = binding.appBar.toolbar
        setSupportActionBar(toolbar)
        drawerLayout = binding.drawerLayout

        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
        val headView = navView.getHeaderView(0)

        if (user!!.type == "Startuper") {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.list_of_startups, R.id.messages, R.id.my_startups
                ), drawerLayout
            )
            navView.inflateMenu(R.menu.activity_startuper_drawer)
        } else {
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.list_of_startups, R.id.messages, R.id.favorite_startups
                ), drawerLayout
            )
            navView.inflateMenu(R.menu.activity_investor_drawer)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val textUserEmail = headView.findViewById<TextView>(R.id.user_email)
        textUserEmail.text = user.email
        val textUserName = headView.findViewById<TextView>(R.id.user_name)
        textUserName.text = user.fullName
        val userIcon = headView.findViewById<ImageView>(R.id.user_icon)
        if (user.icon.uri != "")
            Picasso.get()
                .load(user.icon.uri.toUri())
                .fit().centerCrop()
                .into(userIcon)

        val settingsButton = headView.findViewById<ImageView>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun setDrawerLocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        //toolbar.navigationIcon = null
    }

    override fun setDrawerUnlocked() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}