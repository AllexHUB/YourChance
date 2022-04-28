package com.grigorenko.yourchance.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.grigorenko.yourchance.database.viewmodel.UserViewModel
import com.grigorenko.yourchance.databinding.ActivityMainBinding
import com.grigorenko.yourchance.ui.list_of_startups.NavigationDrawerDisabler
import com.grigorenko.yourchance.ui.list_of_startups.tablayout.SelectedStartupFragment
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), NavigationDrawerDisabler {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var userViewModel: UserViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userUID = userViewModel.getCurrentUserUID()
        userViewModel.getUserByUID(userUID)

        toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_list_of_startups, R.id.nav_messages, R.id.nav_my_startup
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headVIew = navView.getHeaderView(0)

        userViewModel.userModel.observe(this) {
            if (it != null) {
                val textUserEmail = headVIew.findViewById<TextView>(R.id.user_email)
                textUserEmail.text = it.email
                val textUserName = headVIew.findViewById<TextView>(R.id.user_name)
                textUserName.text = it.fullName
                val userIcon = headVIew.findViewById<ImageView>(R.id.user_icon)
                if (it.icon.uri != "")
                    Picasso.get()
                        .load(it.icon.uri.toUri())
                        .fit().centerCrop()
                        .into(userIcon)
            }
        }

        val settingsButton = headVIew.findViewById<ImageView>(R.id.settings_button)
//        settingsButton.setOnClickListener {
//            val fragment = supportFragmentManager.findFragmentById()
//            val tt = R.layout.fragment_selected_startup
//            if (fragment != null)
//                Toast.makeText(this, "NOT NULL", Toast.LENGTH_SHORT).show()
//            else
//                Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
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