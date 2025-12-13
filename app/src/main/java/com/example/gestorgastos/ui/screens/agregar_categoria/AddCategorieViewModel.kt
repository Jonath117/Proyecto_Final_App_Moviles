package com.example.gestorgastos.ui.screens.add_category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gestorgastos.data.ExpenseRepository

class AddCategoryViewModel : ViewModel() {
    var categoryName by mutableStateOf("")
        private set

    fun onNameChange(text: String) {
        categoryName = text
    }

    fun saveCategory() {
        if (categoryName.isNotBlank()) {
            ExpenseRepository.addCategory(categoryName)
            categoryName = "" // Limpiar
        }
    }
}