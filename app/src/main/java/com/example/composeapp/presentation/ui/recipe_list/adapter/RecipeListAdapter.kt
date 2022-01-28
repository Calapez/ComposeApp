package com.example.composeapp.presentation.ui.recipe_list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.composeapp.databinding.RecipeListItemBinding
import com.example.composeapp.domain.model.Recipe


interface RecipeListInteraction {
    fun onClick(position: Int)
}

class RecipeListAdapter(
    private val interaction: RecipeListInteraction
) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallback()) {

    private val poiList: MutableList<Recipe> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding = RecipeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding, interaction)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) =
        holder.bind(getItem(position), position)

    override fun getItemCount() = poiList.size
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}