package com.example.composeapp.network.responses

import com.example.composeapp.network.model.RecipeDto
import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse (

    @SerializedName("count")
    val count: Int,

    @SerializedName("next")
    val next: String? = null,

    @SerializedName("previous")
    val previous: String? = null,

    @SerializedName("results")
    val recipes: List<RecipeDto> = listOf(),
)