package com.example.gestorgastos.ui.screens.detalle_gasto

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.domain.model.ExpenseItem
import com.example.gestorgastos.data.ExpenseRepository
import com.example.gestorgastos.domain.model.Category
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExpenseDetailViewModel : ViewModel() {

    var expense by mutableStateOf<ExpenseItem?>(null)
        private set

    fun loadExpense(id: String) {
        viewModelScope.launch {
            ExpenseRepository.expenses.collectLatest { list ->
                expense = list.find { it.id == id }
            }
        }
    }
}