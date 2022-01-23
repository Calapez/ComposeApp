package com.example.composeapp.presentation.ui.recipe_list

import com.example.composeapp.domain.model.Recipe

sealed class RecipeListEvent {

    object NewSearchEvent: RecipeListEvent()

    object NextPageEvent: RecipeListEvent()

    // restore after process death
    object RestoreStateEvent: RecipeListEvent()

}