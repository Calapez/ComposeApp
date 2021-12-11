package com.example.composeapp.domain.model

data class Recipe(
    val id: Int? = null,
    val title: String? = "",
    val publisher: String? = "",
    val imageUrl: String? = "",
    val rating: Int? = 0,
    val sourceUrl: String? = "",
    val description: String? = "",
    val instructions: String? = "",
    val ingredients: List<String>? = listOf(),
    val dateAdded: String? = "",
    val dateUpdated: String? = ""
)
