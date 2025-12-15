package com.example.gestorgastos.ui.screens.reporteria

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.sortedByDescending
import com.example.gestorgastos.domain.model.CategoryReportItem


class ReporteriaViewModel : ViewModel() {

    val reportData = ExpenseRepository.expenses.map { expenses ->
        val totalExpense = expenses.sumOf { it.amount }

        // Agrupamos por nombre de categoría
        val grouped = expenses.groupBy { it.categoryName }

        grouped.map { (categoryName, list) ->
            val categoryTotal = list.sumOf { it.amount }
            val categoryInfo = ExpenseRepository.getCategoryByName(categoryName)

            CategoryReportItem(
                name = categoryName,
                totalAmount = categoryTotal,
                color = categoryInfo?.color ?: Color.Gray,
                percentage = if (totalExpense > 0) (categoryTotal / totalExpense).toFloat() else 0f
            )
        }.sortedByDescending { it.percentage }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalSpent = ExpenseRepository.expenses.map { list ->
        list.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )
}