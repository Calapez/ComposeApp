package com.example.composeapp.presentation.ui.recipe_list

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

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query: MutableState<String> = mutableStateOf("")
    val loading: MutableState<Boolean> = mutableStateOf(false)

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

    /**
     * Called when a new search is executed.
     */
    private fun resetSearchState(){
        recipes.value = listOf()
    }
}