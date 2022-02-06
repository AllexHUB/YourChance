package com.grigorenko.yourchance.ui.user_profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grigorenko.yourchance.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }
}