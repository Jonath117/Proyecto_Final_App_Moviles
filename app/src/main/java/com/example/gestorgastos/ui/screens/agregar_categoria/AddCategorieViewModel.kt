package com.example.gestorgastos.ui.screens.add_category

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorgastos.data.ExpenseRepository
import com.example.gestorgastos.domain.model.Category
import kotlinx.coroutines.launch
import java.util.UUID

class AddCategoryViewModel : ViewModel() {
    var categoryName by mutableStateOf("")
        private set

    var isSaved by mutableStateOf(false)
        private set

    // Variable para controlar loading (opcional pero recomendado)
    var isLoading by mutableStateOf(false)
        private set

    val availableColors = listOf(
        Color(0xFFEF5350), // Rojo
        Color(0xFFEC407A), // Rosa
        Color(0xFFAB47BC), // Púrpura
        Color(0xFF7E57C2), // Violeta (Tu color principal)
        Color(0xFF5C6BC0), // Índigo
        Color(0xFF42A5F5), // Azul
        Color(0xFF26A69A), // Teal
        Color(0xFF66BB6A), // Verde
        Color(0xFFFFCA28), // Ámbar
        Color(0xFFFF7043), // Naranja
        Color(0xFF8D6E63), // Marrón
        Color(0xFF78909C)  // Gris Azulado
    )

    val availableIcons = listOf(
        Icons.Default.Home,
        Icons.Default.ShoppingCart,
        Icons.Default.Restaurant,
        Icons.Default.DirectionsCar,
        Icons.Default.Flight,
        Icons.Default.SportsEsports,
        Icons.Default.FitnessCenter,
        Icons.Default.School,
        Icons.Default.Work,
        Icons.Default.Pets,
        Icons.Default.LocalHospital,
        Icons.Default.LocalCafe,
        Icons.Default.Terrain,
        Icons.Default.Explore,
        Icons.Default.Hiking,
        Icons.Default.BeachAccess,
        Icons.Default.AttachMoney,
        Icons.Default.Receipt,
        Icons.Default.AccountBalance,
        Icons.Default.CreditCard,
        Icons.Default.TrendingUp,
        Icons.Default.Build,
        Icons.Default.Phone,
        Icons.Default.Lightbulb,

    )

    var selectedColor by mutableStateOf(availableColors[0])
        private set

    var selectedIcon by mutableStateOf(availableIcons[0])
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var hasInteracted by mutableStateOf(false)
        private set

    fun onColorSelected(color: Color) { selectedColor = color }
    fun onIconSelected(icon: ImageVector) { selectedIcon = icon }

    fun validateOnSubmit() {
        hasInteracted = true
        validateName()
    }

    fun onNameChange(text: String) {
        categoryName = text
        hasInteracted = true
        validateName()
    }

    private fun validateName() {
        nameError = when {
            categoryName.isBlank() ->
                "El nombre no puede estar vacío"

            categoryName.length < 3 ->
                "Debe tener al menos 3 caracteres"

            else -> null
        }
    }

    fun isFormValid(): Boolean =
        nameError == null && categoryName.isNotBlank()

    fun saveCategory() {
        if (categoryName.isNotBlank()) {

            viewModelScope.launch {
                isLoading = true // Bloqueamos botón
                try {
                    val newCategory = Category(
                        id = UUID.randomUUID().toString(),
                        name = categoryName,
                        icon = selectedIcon,
                        color = selectedColor
                    )


                    ExpenseRepository.addCategory(newCategory)

                    isSaved = true

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading = false
                }
            }
        }
    }
}