package com.example.composeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.repository.IRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: IRecipeRepository,
    var token: String
): ViewModel() {
    public val PageSize = 30

    private var scrollPosition = 0

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query: MutableState<String> = mutableStateOf("")
    val loading: MutableState<Boolean> = mutableStateOf(false)
    val page: MutableState<Int> = mutableStateOf(1)

    init {
        viewModelScope.launch {
            val result = repository.search(token, 1, "")
            recipes.value = result
        }
    }

    fun onQueryChanged(query: String) {
        this.query.value = query

        resetSearchState()

        viewModelScope.launch {
            loading.value = true
            val result = repository.search(token, 1, query)
            loading.value = false

            recipes.value = result
        }
    }

    fun nextPage() {
        viewModelScope.launch {
            // prevent duplicat eevents due to recompose happening too quickly
            if ((scrollPosition + 1) >= (page.value * PageSize)) {
                loading.value = true
                incrementPage()
                Log.d("vm", "nextPage: triggered ${page.value}")

                // Prevents this to be called on page initial load
                if (page.value > 1) {
                    val result = repository.search(token, page.value, query.value)
                    appendRecipes(result)
                }
                loading.value = false
            }
        }
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        scrollPosition = position
    }

    private fun incrementPage() {
        page.value = page.value + 1
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    /**
     * Called when a new search is executed.
     */
    private fun resetSearchState(){
        recipes.value = listOf()
        page.value = 1
        scrollPosition = 0
    }
}