package com.grigorenko.yourchance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.grigorenko.yourchance.databinding.ActivityMainBinding
import com.grigorenko.yourchance.ui.user_profile.UserProfileActivity
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = intent.getStringExtra("Email")
        val userName = intent.getStringExtra("Name")
        val userPhoneNumber = intent.getStringExtra("Phone")
        val userPhoto = intent.getParcelableExtra<Uri>("Photo")

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
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
        headVIew.apply {
            val textUserEmail = this.findViewById<TextView>(R.id.user_email)
            textUserEmail.text = userEmail
            val textUserName = this.findViewById<TextView>(R.id.user_name)
            textUserName.text = userName
            val textUserPhoto = this.findViewById<ImageView>(R.id.user_icon)
            Log.d("123", "Uri user photo - $userPhoto")
            Picasso.get()
                .load(userPhoto)
                .fit().centerCrop()
                .into(textUserPhoto)
        }

        val settingsButton = headVIew.findViewById<ImageView>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val i = Intent(applicationContext, UserProfileActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}