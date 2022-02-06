package com.grigorenko.yourchance.ui.my_startup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyStartupViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is My Startup!"
    }
    val text: LiveData<String> = _text
}