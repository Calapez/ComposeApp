package com.example.composeapp.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.Assisted
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.presentation.ui.recipe.RecipeEvent.*
import com.example.composeapp.presentation.ui.recipe_list.RecipeListEvent.*
import com.example.composeapp.repository.IRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

const val PAGE_SIZE = 30

const val STATE_KEY_RECIPE_ID = "recipe.state.recipeId.key"

@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    private val repository: IRecipeRepository,
    var token: String,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val recipe: MutableLiveData<Recipe?> = MutableLiveData(null)
    val loading = MutableLiveData(false)

    init {
        savedStateHandle.get<Int>(STATE_KEY_RECIPE_ID)?.let { recipeId ->
            onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

    // Only entrance to trigger events in ViewModel from the Fragment
    fun onTriggerEvent(event: RecipeEvent) {
        viewModelScope.launch {
            try {
                when(event) {
                    is GetRecipeEvent -> {
                        if (recipe.value == null) {
                            getRecipe(event.recipeId)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("viewmodel", "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
    }

    private suspend fun getRecipe(id: Int) {
        loading.value = true
        val result = repository.get(token, id)
        recipe.value = result
        savedStateHandle.set(STATE_KEY_RECIPE_ID, id)
        loading.value = false
    }
}