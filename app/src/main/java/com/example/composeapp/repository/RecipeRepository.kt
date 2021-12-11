package com.example.composeapp.repository

import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.network.IRecipeService
import com.example.composeapp.network.model.RecipeDtoMapper

class RecipeRepository(
    private val recipeService: IRecipeService,
    private val mapper: RecipeDtoMapper
) : IRecipeRepository {

    override suspend fun search(token: String, page: Int, query: String): List<Recipe> {
        val recipeDtos = recipeService.search(token, page, query).recipes
        return mapper.toDomainList(recipeDtos)
    }

    override suspend fun get(token: String, id: Int): Recipe {
        val recipeDto = recipeService.get(token, id)
        return mapper.mapToDomainModel(recipeDto)
    }

}