package com.example.gestorgastos.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ExpenseItem(
    val id: String,
    val title: String,
    val amount: Double,

    @SerialName("category_name")
    val categoryName: String,
    val date: Long,

    @SerialName("image_uris")
    val imageUris: List<String>? = null

){
    val safeImages: List<String>
        get() = imageUris ?: emptyList()
}