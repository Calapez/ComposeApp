package com.example.composeapp.presentation.ui.recipe_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.composeapp.databinding.RecipeListItemBinding
import com.example.composeapp.domain.model.Recipe


interface RecipeListInteraction {
    fun onClick(position: Int)
}

class RecipeListAdapter(
    private val interaction: RecipeListInteraction
) : RecyclerView.Adapter<RecipeViewHolder>() {

    private val recipeList: MutableList<Recipe> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding = RecipeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding, interaction)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) =
        holder.bind(recipeList[position], position)

    override fun getItemCount() = recipeList.size

    fun updateList(recipes: List<Recipe>) {
        recipeList.apply {
            clear()
            addAll(recipes)
        }
        notifyDataSetChanged()
    }
}