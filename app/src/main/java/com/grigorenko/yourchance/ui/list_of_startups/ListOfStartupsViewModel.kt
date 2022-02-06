package com.grigorenko.yourchance.ui.list_of_startups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListOfStartupsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is List of Startups!"
    }
    val text: LiveData<String> = _text
}