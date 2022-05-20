package com.grigorenko.yourchance.auth.tablayout.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.grigorenko.yourchance.databinding.ActivityAuthenticationBinding
import com.grigorenko.yourchance.auth.tablayout.adapter.ViewPagerAdapter

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Авторизация"
                1 -> tab.text = "Регистрация"
            }
        }.attach()
    }

    fun disableTabLayout() {
        tabLayout.visibility = View.GONE
    }

    fun enableTabLayout() {
        tabLayout.visibility = View.VISIBLE
    }
}