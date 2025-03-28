package com.taskmanagerapp.taskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanagerapp.taskmanager.data.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application)

    val darkModeFlow: Flow<Boolean> = dataStoreManager.darkModeFlow

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkMode(enabled)
        }
    }
}
