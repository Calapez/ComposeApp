package com.example.composeapp.presentation.ui.recipe

import com.example.composeapp.domain.model.Recipe

sealed class RecipeEvent {

    data class GetRecipeEvent(val recipeId: Int): RecipeEvent()

}