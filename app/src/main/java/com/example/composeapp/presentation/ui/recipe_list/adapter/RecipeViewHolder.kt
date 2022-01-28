package com.example.composeapp.presentation.ui.recipe_list.adapter

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

            recipeName.text = recipe.title

            recipeCard.setOnClickListener {
                interaction.onClick(position)
            }
        }
    }
}