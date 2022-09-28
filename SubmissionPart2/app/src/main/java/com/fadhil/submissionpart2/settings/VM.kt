package com.fadhil.submissionpart2.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class VM (private val preferences: SettingPreferences): ViewModel(){
    fun getTheme() = preferences.getThemeSetting().asLiveData()

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            VM(preferences) as T
    }
}