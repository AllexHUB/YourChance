package com.grigorenko.yourchance.ui.user_profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.grigorenko.yourchance.auth.tablayout.activity.AuthenticationActivity
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.viewmodel.AuthViewModel
import com.grigorenko.yourchance.database.viewmodel.UserViewModel
import com.grigorenko.yourchance.databinding.ActivityUserProfileBinding
import com.squareup.picasso.Picasso

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var icon: Image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setSupportActionBar(binding.toolbar)

        binding.apply {
            buttonSignOut.setOnClickListener {
                authViewModel.userSignOut()
                val authActivity = Intent(this@UserProfileActivity, AuthenticationActivity::class.java)
                startActivity(authActivity)
                finishAffinity()
            }
            buttonChooseImage.setOnClickListener {
                openFileChooser()
            }
            buttonSaveImage.setOnClickListener {
                val userUID = userViewModel.getCurrentUserUID()
                userViewModel.updateUserIcon(userUID, icon, binding.userIcon.drawable)
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
        activityResult.launch(intent)
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                val generatedName = System.currentTimeMillis().toString()
                icon = Image(generatedName, it.data!!.data.toString())
                Picasso.get()
                    .load(it.data!!.data)
                    .fit().centerCrop()
                    .into(binding.userIcon)
            }
        }
}