package com.example.gestorgastos.ui.screens.add_expense

import android.content.Context
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.data.ExpenseRepository
import com.example.gestorgastos.domain.model.Category
import com.example.gestorgastos.domain.model.ExpenseItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

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

    var amountError by mutableStateOf<String?>(null)
        private set

    var isSaved by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set


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

    var hasInteracted by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    fun onAmountChange(text: String) { amount = text }
    fun onDetailChange(text: String) { detail = text }
    fun onCategorySelected(category: Category) { selectedCategory = category }
    fun onDateChange(newDate: Long) { dateMillis = newDate }

    fun saveExpense(context: Context) {
        hasInteracted = true

        amountError = null
        errorMessage = null
        isSuccess = false

        if (amount.isBlank()) {
            amountError = "Ingresa un monto"
            return
        }
        val doubleAmount = amount.replace(",", ".").toDoubleOrNull()
        if (doubleAmount == null || doubleAmount <= 0) {
            amountError = "El monto debe ser mayor a 0"
            return
        }

        if (selectedCategory == null) {
            errorMessage = "Selecciona una categoría"
            return
        }
        viewModelScope.launch {
            isLoading = true
            try {
                val finalImageUrls = selectedImages.map { uri ->
                    val uriString = uri.toString()
                    if (uriString.startsWith("http")) uriString
                    else ExpenseRepository.uploadImage(context, uri)
                }

                val finalId = editingExpenseId ?: UUID.randomUUID().toString()

                val newItem = ExpenseItem(
                    id = finalId,
                    title = detail.ifBlank { selectedCategory!!.name },
                    amount = doubleAmount,
                    categoryName = selectedCategory!!.name,
                    date = dateMillis,
                    imageUris = finalImageUrls
                )

                if (editingExpenseId == null) {
                    ExpenseRepository.addExpense(newItem)
                } else {
                    ExpenseRepository.updateExpense(newItem)
                }

                isSaved = true
                isSuccess = true
                clearForm()

            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun clearForm() {
        amount = ""
        detail = ""
        selectedCategory = null
        selectedImages = emptyList()
        editingExpenseId = null
        hasInteracted = false
        amountError = null
    }

    fun onImagesSelected(uris: List<Uri>) {
        val combined = selectedImages + uris
        selectedImages = combined.take(2)
    }

    fun removeImage(uri: Uri) {
        selectedImages = selectedImages - uri
    }

    fun isFormValid(): Boolean {
        return amount.toDoubleOrNull() != null &&
                amount.toDouble() > 0 &&
                selectedCategory != null
    }

    fun loadExpenseIfEditing(id: String?) {
        if (id == null) return

        // Intentamos obtenerlo del repositorio local (memoria) para rellenar rápido el formulario
        val expense = ExpenseRepository.getExpenseById(id) ?: return

        editingExpenseId = expense.id
        amount = expense.amount.toString().replace(".", ",")
        detail = expense.title
        dateMillis = expense.date
        selectedCategory = ExpenseRepository.getCategoryByName(expense.categoryName)

        // Convertimos las URLs (Strings) a URIs para que Coil las pueda mostrar
        selectedImages = expense.safeImages.map { Uri.parse(it) }
    }
}