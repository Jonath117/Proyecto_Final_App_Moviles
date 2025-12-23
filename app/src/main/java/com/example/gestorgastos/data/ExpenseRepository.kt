package com.example.gestorgastos.data

import android.content.Context
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.gestorgastos.domain.model.Category
import com.example.gestorgastos.domain.model.ExpenseItem
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.UUID
import android.util.Log
import androidx.compose.material.icons.automirrored.filled.TrendingUp

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

object ExpenseRepository {

    private const val TAG = "SupabaseRepo"

    private val _expenses = MutableStateFlow<List<ExpenseItem>>(emptyList())
    val expenses: StateFlow<List<ExpenseItem>> = _expenses.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val defaultCategoriesList = listOf(
        Category(UUID.randomUUID().toString(), "Transporte", Icons.Default.DirectionsBus, Color(0xFF4FC3F7)),
        Category(UUID.randomUUID().toString(), "Salud", Icons.Default.Favorite, Color(0xFFEF5350)),
        Category(UUID.randomUUID().toString(), "Casa", Icons.Default.Home, Color(0xFF66BB6A)),
        Category(UUID.randomUUID().toString(), "Regalos", Icons.Default.CardGiftcard, Color(0xFF8D6E63)),
        Category(UUID.randomUUID().toString(), "Comida", Icons.Default.Restaurant, Color(0xFFFFA726)),
        Category(UUID.randomUUID().toString(), "Familia", Icons.Default.FamilyRestroom, Color(0xFFAB47BC)),
        Category(UUID.randomUUID().toString(), "Ocio", Icons.Default.Weekend, Color(0xFF78909C))
    )

    suspend fun fetchAllData() {
        Log.d(TAG, "Iniciando descarga de datos...")
        getExpenses()
        getCategories()
    }

    suspend fun getExpenses() {
        withContext(Dispatchers.IO) {
            try {
                val list = SupabaseClient.client.from("expenses")
                    .select()
                    .decodeList<ExpenseItem>()
                _expenses.value = list
                Log.d(TAG, "Gastos descargados: ${list.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error al bajar gastos: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // CREATE (CON TRY CATCH AHORA)
    suspend fun addExpense(expense: ExpenseItem) {
        withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.from("expenses").insert(expense)
                Log.d(TAG, "Gasto subido correctamente")
                getExpenses()
            } catch (e: Exception) {
                Log.e(TAG, "Error al subir gasto: ${e.message}")
            }
        }
    }

    // ... (Mantén deleteExpense y updateExpense igual pero agrégales try-catch como arriba) ...

    suspend fun getCategories() {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Intentando bajar categorías...")
                val dtos = SupabaseClient.client.from("categories")
                    .select()
                    .decodeList<CategoryDto>()

                Log.d(TAG, "Categorías encontradas en nube: ${dtos.size}")

                if (dtos.isEmpty()) {
                    Log.d(TAG, "Nube vacía. Subiendo defaults...")
                    seedDefaultCategories()
                    getCategories() // Reintentar
                } else {
                    val domainList = dtos.map { dto ->
                        // Convertir Int/Long a Color de forma segura
                        val colorInt = dto.colorHex.toInt()
                        Category(
                            id = dto.id,
                            name = dto.name,
                            icon = getIconByName(dto.iconName),
                            color = Color(colorInt)
                        )
                    }
                    _categories.value = domainList
                    Log.d(TAG, "Categorías actualizadas en UI")
                }
            } catch (e: Exception) {
                Log.e(TAG, "CRITICAL ERROR en getCategories: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private suspend fun seedDefaultCategories() {
        try {
            val dtosToUpload = defaultCategoriesList.map { category ->
                CategoryDto(
                    id = category.id,
                    name = category.name,
                    iconName = getNameByIcon(category.icon),
                    colorHex = category.color.toArgb().toLong()
                )
            }
            SupabaseClient.client.from("categories").insert(dtosToUpload)
            Log.d(TAG, "Defaults subidos con éxito")
        } catch (e: Exception) {
            Log.e(TAG, "Error al subir defaults: ${e.message}")
        }
    }

    suspend fun addCategory(category: Category) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Subiendo nueva categoría: ${category.name}")
                val dto = CategoryDto(
                    id = category.id,
                    name = category.name,
                    iconName = getNameByIcon(category.icon),
                    colorHex = category.color.toArgb().toLong()
                )
                SupabaseClient.client.from("categories").insert(dto)
                Log.d(TAG, "Categoría creada éxito")
                getCategories()
            } catch (e: Exception) {
                // AQUÍ ES DONDE EVITAMOS EL CRASH
                Log.e(TAG, "Error al crear categoría: ${e.message}")
                e.printStackTrace()
            }
        }
    }




    // DELETE
    suspend fun deleteExpense(expenseId: String) {
        withContext(Dispatchers.IO) {
            SupabaseClient.client.from("expenses").delete {
                filter {
                    eq("id", expenseId)
                }
            }
            getExpenses()
        }
    }

    // UPDATE
    suspend fun updateExpense(updatedItem: ExpenseItem) {
        withContext(Dispatchers.IO) {
            SupabaseClient.client.from("expenses").update(updatedItem) {
                filter {
                    eq("id", updatedItem.id)
                }
            }
            getExpenses()
        }
    }

    // STORAGE (Fotos)
    suspend fun uploadImage(context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val bucket = SupabaseClient.client.storage.from("receipts")
            val fileName = "${UUID.randomUUID()}.jpg"
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw Exception("No se pudo leer la imagen")

            bucket.upload(fileName, bytes)
            bucket.publicUrl(fileName)
        }
    }

    fun getExpenseById(id: String): ExpenseItem? {
        return _expenses.value.find { it.id == id }
    }

    // DELETE
    suspend fun deleteCategory(categoryId: String) {
        withContext(Dispatchers.IO) {
            SupabaseClient.client.from("categories").delete {
                filter {
                    eq("id", categoryId)
                }
            }
            getCategories() // Recargamos
        }
    }


    fun getCategoryByName(name: String): Category? {
        return _categories.value.find { it.name == name }
    }
}


val iconMap = mapOf(
    "DirectionsBus" to Icons.Default.DirectionsBus,
    "Favorite" to Icons.Default.Favorite,
    "CardGiftcard" to Icons.Default.CardGiftcard,
    "FamilyRestroom" to Icons.Default.FamilyRestroom,
    "Weekend" to Icons.Default.Weekend,

    "Home" to Icons.Default.Home,
    "ShoppingCart" to Icons.Default.ShoppingCart,
    "Restaurant" to Icons.Default.Restaurant,
    "DirectionsCar" to Icons.Default.DirectionsCar,
    "Flight" to Icons.Default.Flight,
    "SportsEsports" to Icons.Default.SportsEsports,
    "FitnessCenter" to Icons.Default.FitnessCenter,
    "School" to Icons.Default.School,
    "Work" to Icons.Default.Work,
    "Pets" to Icons.Default.Pets,
    "LocalHospital" to Icons.Default.LocalHospital,
    "LocalCafe" to Icons.Default.LocalCafe,
    "Terrain" to Icons.Default.Terrain,
    "Explore" to Icons.Default.Explore,
    "Hiking" to Icons.Default.Hiking,
    "BeachAccess" to Icons.Default.BeachAccess,
    "AttachMoney" to Icons.Default.AttachMoney,
    "Receipt" to Icons.Default.Receipt,
    "AccountBalance" to Icons.Default.AccountBalance,
    "CreditCard" to Icons.Default.CreditCard,
    "Build" to Icons.Default.Build,
    "Phone" to Icons.Default.Phone,
    "Lightbulb" to Icons.Default.Lightbulb,


    "TrendingUp" to Icons.AutoMirrored.Filled.TrendingUp,
    "MoneyOff" to Icons.Default.MoneyOff 
)
fun getIconByName(name: String): androidx.compose.ui.graphics.vector.ImageVector {
    return iconMap[name] ?: Icons.Default.MoneyOff
}

fun getNameByIcon(icon: androidx.compose.ui.graphics.vector.ImageVector): String {
    return iconMap.entries.find { it.value == icon }?.key ?: "MoneyOff"
}

@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
    @SerialName("icon_name") val iconName: String,
    @SerialName("color_hex") val colorHex: Long
)