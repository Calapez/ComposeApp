package com.example.composeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.presentation.components.RecipeCard
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
    fun RecipesList() {
        val query = viewModel.query.value

        Column(modifier = Modifier.padding(5.dp)) {
            Surface(
                elevation = 8.dp,
                modifier = Modifier.fillMaxWidth()) {

                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = query,
                            onValueChange = { viewModel.onQueryChanged(it) },
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

                    val chips = listOf(
                        "Chicken",
                        "Pie",
                        "Lime",
                        "Beef",
                        "Taco",
                        "Veggie",
                        "Burger",
                        "Potato",
                        "Salad",
                        "Pizza",
                        "Kebab",
                        "Pancake",
                        "Chocolate"
                    )

                    LazyRow(modifier = Modifier.padding(5.dp)) {
                        items(chips) { item ->
                            Button(
                                content = { Text(item) },
                                modifier = Modifier.padding(horizontal = 10.dp),
                                onClick = { viewModel.onQueryChanged(item) },
                                enabled = viewModel.query.value != item
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            val recipes = viewModel.recipes.value

            LazyColumn(modifier = Modifier.padding(vertical = 5.dp)) {
                items(recipes) { recipe ->
                    RecipeCard(recipe = recipe, { })
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

    /*
    @Preview
    @Composable
    fun RecipeListPreview() {
        ComposeRecipeAppTheme {
            RecipesList()
        }
    }

    @Preview
    @Composable
    fun RecipeCardPreview() {
        ComposeRecipeAppTheme {
            RecipeCard(Recipe())
        }
    }

     */
}