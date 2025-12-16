package com.example.gestorgastos.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.data.AppThemeMode
import com.example.gestorgastos.data.ThemeManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel : ViewModel() {

    val currentTheme = ThemeManager.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppThemeMode.SYSTEM
    )

    fun changeTheme(context: Context, mode: AppThemeMode) {
        ThemeManager.saveTheme(context, mode)
    }
}