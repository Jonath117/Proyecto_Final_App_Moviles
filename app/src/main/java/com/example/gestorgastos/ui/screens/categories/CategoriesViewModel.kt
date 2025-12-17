package com.example.gestorgastos.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    // Este ViewModel solo sirve para "leer" la lista de categorías del repositorio
    val categories = ExpenseRepository.categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            ExpenseRepository.deleteCategory(id)
        }
    }
}