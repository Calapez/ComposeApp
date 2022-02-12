package com.example.composeapp.presentation.ui.recipe_list.adapter

import android.os.Build
import android.text.Html
import androidx.core.text.htmlEncode
import androidx.recyclerview.widget.RecyclerView
import com.example.composeapp.R
import com.example.composeapp.databinding.RecipeListItemBinding
import com.example.composeapp.domain.model.Recipe
import com.squareup.picasso.Picasso

class RecipeViewHolder(
    private val binding: RecipeListItemBinding,
    private val interaction: RecipeListInteraction
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe, position: Int) {
        binding.apply {
            Picasso.get()
                .load(recipe.imageUrl)
                .placeholder(R.drawable.empty_plate)
                .error(R.drawable.empty_plate)
                .into(recipeImage)

            recipeName.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(recipe.title, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(recipe.title).toString()
            }

            recipeCard.setOnClickListener {
                interaction.onClick(position, recipe)
            }
        }
    }
}