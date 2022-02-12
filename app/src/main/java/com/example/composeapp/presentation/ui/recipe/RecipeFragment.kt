package com.example.composeapp.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.composeapp.R
import com.example.composeapp.databinding.FragmentRecipeBinding
import com.example.composeapp.databinding.FragmentRecipeListBinding
import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.presentation.ui.recipe_list.adapter.RecipeListAdapter
import com.example.composeapp.presentation.ui.recipe_list.adapter.RecipeListInteraction
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    private lateinit var binding: FragmentRecipeBinding

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
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            setRecipeLayout(recipe)
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.contentLayout.visibility = View.GONE
                binding.shimmerLayout.startShimmer()
            } else {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.contentLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setRecipeLayout(recipe: Recipe?) {
        if (recipe != null) {
            Picasso.get()
                .load(recipe.imageUrl)
                .placeholder(R.drawable.empty_plate)
                .error(R.drawable.empty_plate)
                .into(binding.recipeImage)

            binding.recipeTitle.text = recipe.title

            binding.recipeRating.text = recipe.rating?.toString() ?: "N/A"

            binding.recipePublisher.text = if(recipe.dateUpdated != null) {
                "Updated ${recipe.dateUpdated} by ${recipe.publisher}"
            } else {
                "By ${recipe.publisher}"
            }

            if (recipe.description != "N/A") {
                binding.recipeDescription.text = recipe.description
            }

            var ingredientsText = ""
            recipe.ingredients?.forEach {
                ingredientsText += it + "\n"
            }
            binding.recipeIngredients.text = ingredientsText

            binding.recipeInstructions.text = recipe.instructions
        }
    }
}