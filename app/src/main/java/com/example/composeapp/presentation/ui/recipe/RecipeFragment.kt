package com.example.composeapp.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.composeapp.presentation.components.CircularIndeterminateProgressBar
import com.example.composeapp.presentation.components.DEFAULT_EMPTY_IMAGE
import com.example.composeapp.presentation.ui.recipe_list.RecipeListViewModel
import com.example.composeapp.util.loadPicture
import dagger.hilt.android.AndroidEntryPoint

const val IMAGE_HEIGHT = 260

@AndroidEntryPoint
class RecipeFragment : Fragment () {

    val viewModel: RecipeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeId")?.let{
            viewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(it))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val loading = viewModel.loading.value
                val recipe = viewModel.recipe.value

                if (loading) {
                    CircularIndeterminateProgressBar()
                } else if (recipe != null) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        recipe.imageUrl?.let { url ->
                            val image = loadPicture(
                                url = url,
                                defaultImage = DEFAULT_EMPTY_IMAGE).value

                            image?.let { img ->
                                Image(
                                    bitmap = img.asImageBitmap(),
                                    contentDescription = "Image of Recipe: ${recipe.title}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IMAGE_HEIGHT.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            recipe.title?.let { title ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 4.dp)
                                ){
                                    Text(
                                        text = title,
                                        modifier = Modifier
                                            .fillMaxWidth(0.85f)
                                            .wrapContentWidth(Alignment.Start)
                                        ,
                                        style = MaterialTheme.typography.h3
                                    )
                                    val rank = recipe.rating.toString()
                                    Text(
                                        text = rank,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(Alignment.End)
                                            .align(Alignment.CenterVertically)
                                        ,
                                        style = MaterialTheme.typography.h5
                                    )
                                }
                            }
                            recipe.publisher?.let { publisher ->
                                val updated = recipe.dateUpdated
                                Text(
                                    text = if(updated != null) {
                                        "Updated ${updated} by ${publisher}"
                                    }
                                    else {
                                        "By ${publisher}"
                                    }
                                    ,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                    ,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                            recipe.description?.let { description ->
                                if(description != "N/A"){
                                    Text(
                                        text = description,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                        ,
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            }
                            recipe.ingredients?.forEach { ingredient ->
                                Text(
                                    text = ingredient,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 4.dp),
                                    style = MaterialTheme.typography.body1
                                )
                            }

                            recipe.instructions?.let { instructions ->
                                Text(
                                    text = instructions,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 4.dp)
                                    ,
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}