package com.example.composeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.composeapp.R
import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.presentation.components.*
import com.example.composeapp.presentation.components.HeartButtonState.*
import com.example.composeapp.presentation.ui.recipe_list.RecipeListEvent.*
import com.example.composeapp.util.MockRecipes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RecipesList()
            }
        }
    }

    @Composable
    fun RecipesList(
        recipes: List<Recipe> = viewModel.recipes.value
    ) {
        val state = remember { mutableStateOf(ACTIVE) }

        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedHeartButton(
                modifier = Modifier,
                buttonState = state,
                onToggle = { state.value = if(state.value == IDLE) ACTIVE else IDLE }
            )

            Surface(
                elevation = 8.dp,
                modifier = Modifier.fillMaxWidth()) {

                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = viewModel.query.value,
                            onValueChange = {
                                viewModel.onQueryChanged(it)
                            },
                            label = { Text("Search") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
                            leadingIcon = { Icon(Icons.Filled.Search, "test") },
                            textStyle = TextStyle(color = MaterialTheme.colors.primary),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.surface,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    LazyRow(modifier = Modifier.padding(5.dp)) {
                        items(getAllFoodCategories()) { category ->
                            FoodCategoryChip(
                                category = category,
                                isSelected = viewModel.query.value == category.value,
                                onExecuteSearch = {
                                    val text = when (viewModel.query.value) {
                                        category.value -> ""
                                        else -> category.value
                                    }

                                    viewModel.onQueryChanged(text)
                                }
                            )
                        }
                    }
                }
            }

            val loading = viewModel.loading.value
            if (loading) {
                CircularIndeterminateProgressBar()
            }

            val page = viewModel.page.value

            LazyColumn {
                itemsIndexed(
                    items = recipes,
                    key = { _, recipe -> recipe.id ?: 0 }
                ) { index, recipe ->
                    viewModel.onChangeRecipeScrollPosition(index)
                    if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                        viewModel.onTriggerEvent(NextPageEvent)
                    }
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            if (recipe.id != null) {
                                val bundle = Bundle().also { it.putInt("recipeId", recipe.id) }
                                findNavController().navigate(R.id.viewRecipe, bundle)
                            }
                        })
                }
            }
        }
    }

    companion object {
        val DefaultRecipes = listOf(
            Recipe(),
            Recipe(),
            Recipe())
    }

    @Preview
    @Composable
    fun RecipeListPreview() {
        val state = remember { mutableStateOf(MockRecipes.mockRecipeList) }
        RecipesList(state.value)
    }
}