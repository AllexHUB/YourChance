package com.grigorenko.yourchance.ui.user_profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grigorenko.yourchance.MainActivity
import com.grigorenko.yourchance.auth.AuthenticationActivity
import com.grigorenko.yourchance.databinding.ActivityUserProfileBinding
import com.grigorenko.yourchance.viewmodel.StartuperViewModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private val startuperViewModel = StartuperViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.signOutButton.setOnClickListener {
            startuperViewModel.userSignOut()
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }
    }
}