package com.example.gestorgastos.ui.screens.add_expense

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.domain.model.ExpenseItem
import com.example.gestorgastos.data.ExpenseRepository
import java.util.UUID
import com.example.gestorgastos.domain.model.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AddExpenseViewModel : ViewModel() {

    var amount by mutableStateOf("")
        private set
    var detail by mutableStateOf("")
        private set
    var selectedCategory by mutableStateOf<Category?>(null)
        private set

    var dateMillis by mutableStateOf(System.currentTimeMillis())
        private set

    var selectedImages by mutableStateOf<List<Uri>>(emptyList())
        private set

    private var editingExpenseId: String? = null

    val categories = ExpenseRepository.categories.map { list ->
        list + Category(
            id = "create_new",
            name = "Crear",
            icon = Icons.Default.Add,
            color = Color(0xFFE0E0E0)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    var amountError by mutableStateOf<String?>(null)
        private set

    var hasInteracted by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isSuccess by mutableStateOf(false)
        private set


    fun onAmountChange(text: String) { amount = text }
    fun onDetailChange(text: String) { detail = text }
    fun onCategorySelected(category: Category) { selectedCategory = category }

//    fun saveExpense() {
//        if (amount.isNotBlank() && selectedCategory != null) {
//            val newItem = ExpenseItem(
//                id = UUID.randomUUID().toString(),
//                title = detail.ifBlank { selectedCategory!!.name },
//                amount = amount.toDoubleOrNull() ?: 0.0,
//                categoryName = selectedCategory!!.name,
//                date = dateMillis,
//                imageUris = selectedImages.map { it.toString() }
//            )
//            ExpenseRepository.addExpense(newItem)
//
//            // Limpiar
//            amount = ""
//            detail = ""
//            selectedCategory = null
//            selectedImages = emptyList()
//        }
//    }

    fun saveExpense() {
        hasInteracted = true
        errorMessage = null
        isSuccess = false

        when {
            amount.isBlank() -> {
                errorMessage = "Ingresa un monto"
            }

            amount.toDoubleOrNull() == null || amount.toDouble() <= 0 -> {
                errorMessage = "El monto debe ser mayor a 0"
            }

            selectedCategory == null -> {
                errorMessage = "Selecciona una categoría"
            }
            else -> {
                val finalId = editingExpenseId ?: UUID.randomUUID().toString()

                val newItem = ExpenseItem(
                    id = finalId,
                    title = detail.ifBlank { selectedCategory!!.name },
                    amount = amount.replace(",", ".").toDoubleOrNull() ?: 0.0,
                    categoryName = selectedCategory!!.name,
                    date = dateMillis,
                    imageUris = selectedImages.map { it.toString() }
                )

                if (editingExpenseId == null) {
                    ExpenseRepository.addExpense(newItem) // Crear nuevo
                } else {
                    ExpenseRepository.updateExpense(newItem) // Actualizar existente
                }
                isSuccess = true
                clearForm()
            }
        }
    }

    fun clearForm(){
        amount = ""
        detail = ""
        selectedCategory = null
        selectedImages = emptyList()
        editingExpenseId = null
    }

    fun onDateChange(newDate: Long) {
        dateMillis = newDate
    }

    fun onImagesSelected(uris: List<Uri>) {
        val combined = selectedImages + uris
        selectedImages = combined.take(2)
    }

    fun removeImage(uri: Uri) {
        selectedImages = selectedImages - uri
    }

//    fun validateOnSubmit() {
//        hasInteracted = true
//        validateAmount()
//    }
//
//    fun validateAmount(){
//        amountError = when {
//            amount.isBlank() -> "El monto no puede estar vacío"
//            amount.toDoubleOrNull() == null -> "El monto debe ser un número válido"
//            else -> null
//        }
//    }

    fun isFormValid(): Boolean {
        return amount.toDoubleOrNull() != null &&
                amount.toDouble() > 0 &&
                selectedCategory != null
    }

    // NUEVA FUNCIÓN: Cargar datos si es edición
    fun loadExpenseIfEditing(id: String?) {
        if (id == null) return

        val expense = ExpenseRepository.getExpenseById(id) ?: return

        // Rellenamos los campos con los datos existentes
        editingExpenseId = expense.id
        amount = expense.amount.toString().replace(".", ",")
        detail = expense.title
        dateMillis = expense.date

        selectedCategory = ExpenseRepository.getCategoryByName(expense.categoryName)

        selectedImages = expense.imageUris.map { android.net.Uri.parse(it) }
    }



}