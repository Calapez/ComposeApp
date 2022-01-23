package com.example.composeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.presentation.ui.recipe_list.RecipeListEvent.*
import com.example.composeapp.repository.IRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.list_position.key"

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: IRecipeRepository,
    var token: String,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var scrollPosition = 0

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query: MutableState<String> = mutableStateOf("")
    val loading: MutableState<Boolean> = mutableStateOf(false)
    val page: MutableState<Int> = mutableStateOf(1)

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { page ->
            setPage(page)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { query ->
            setQuery(query)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { position ->
            setListScrollPosition(position)
        }

        if (scrollPosition != 0) {
            // User was doing something, must trigger restore event
            onTriggerEvent(RestoreStateEvent)
        } else {
            onTriggerEvent(NewSearchEvent)
        }
    }

    // Only entrance to trigger events in ViewModel from the Fragment
    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when(event) {
                    is NewSearchEvent -> {
                        newSearch()
                    }

                    is NextPageEvent -> {
                        nextPage()
                    }

                    is RestoreStateEvent -> {
                        restoreState()
                    }
                }
            } catch (e: Exception) {
                Log.e("viewmodel", "onTriggrEvent: Exception $e, ${e.cause}")
            }
        }
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
        onTriggerEvent(NewSearchEvent)
    }

    fun onChangeRecipeScrollPosition(position: Int) = setListScrollPosition(position)

    private suspend fun newSearch() {
        resetSearchState()

        loading.value = true
        val result = repository.search(token, 1, query.value)
        loading.value = false

        recipes.value = result
    }

    private suspend fun nextPage() {
        // prevent duplicat eevents due to recompose happening too quickly
        if ((scrollPosition + 1) >= (page.value * PAGE_SIZE)) {
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

    private suspend fun restoreState() {
        loading.value = true
        val results = mutableListOf<Recipe>()
        for (page in 1..page.value) {
            val result = repository.search(token, page, query.value)
            results.addAll(result)

            if (page == this.page.value) {
                recipes.value = results
                loading.value = false
            }
        }
    }

    private fun incrementPage() = setPage(page.value + 1)

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

    private fun setListScrollPosition(position: Int) {
        scrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_LIST_POSITION, page)
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}