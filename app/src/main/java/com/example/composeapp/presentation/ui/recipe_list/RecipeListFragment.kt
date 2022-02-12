package com.example.composeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.composeapp.R
import com.example.composeapp.databinding.FragmentRecipeListBinding
import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.presentation.ui.recipe_list.RecipeListEvent.NextPageEvent
import com.example.composeapp.presentation.ui.recipe_list.adapter.RecipeListAdapter
import com.example.composeapp.presentation.ui.recipe_list.adapter.RecipeListInteraction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment(), RecipeListInteraction {

    private val viewModel: RecipeListViewModel by viewModels()

    private lateinit var binding: FragmentRecipeListBinding

    private var listAdapter: RecipeListAdapter = RecipeListAdapter(this).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy
            .PREVENT_WHEN_EMPTY
    }

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

        binding.searchBar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onQueryChanged(s.toString())
                viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
            }
        })

        binding.recipeList.let { recycler ->
            recycler.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }

            recycler.adapter = listAdapter
        }

        listAdapter.updateList(viewModel.recipes.value!!)

        setupObservers()
    }

    override fun onClick(position: Int, recipe: Recipe) {
        val bundle = Bundle().also { it.putInt("recipeId", recipe.id ?: -1) }
        findNavController().navigate(R.id.viewRecipe, bundle)
    }

    override fun onIndexReached(index: Int) {
        viewModel.onChangeRecipeScrollPosition(index)
        if ((index + 1) >= (viewModel.page.value * PAGE_SIZE) && !viewModel.loading.value!!) {
            viewModel.onTriggerEvent(NextPageEvent)
        }
    }

    private fun setupObservers() {

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            listAdapter.updateList(recipes)
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
             if (loading) {
                 binding.recipeList.visibility = View.GONE
                 binding.shimmerLayout.startShimmer()
             } else {
                 binding.shimmerLayout.stopShimmer()
                 binding.recipeList.visibility = View.VISIBLE
             }
        }
    }
}