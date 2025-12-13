package com.example.gestorgastos.ui.screens.profile

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    var profileImage by mutableStateOf<Bitmap?>(null)
        private set

    fun onImageCaptured(bitmap: Bitmap) {
        profileImage = bitmap
    }
}