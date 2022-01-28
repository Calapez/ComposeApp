package com.example.composeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.composeapp.databinding.FragmentRecipeListBinding
import com.example.composeapp.presentation.ui.recipe_list.adapter.RecipeListAdapter
import com.example.composeapp.presentation.ui.recipe_list.adapter.RecipeListInteraction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment(), RecipeListInteraction{

    private val viewModel: RecipeListViewModel by viewModels()

    private lateinit var binding: FragmentRecipeListBinding

    private var recipeListAdapter: RecipeListAdapter = RecipeListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipeList.let { recycler ->
            recycler.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }

            recycler.adapter = recipeListAdapter
        }

        setupObservers()
    }

    override fun onClick(position: Int) {
        TODO("Not yet implemented")
    }

    private fun setupObservers() {

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipeListAdapter.submitList(recipes)
        }
    }
}