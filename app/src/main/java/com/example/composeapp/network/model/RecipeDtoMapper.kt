package com.example.composeapp.network.model

import com.example.composeapp.domain.model.Recipe
import com.example.composeapp.domain.util.DomainMapper

class RecipeDtoMapper : DomainMapper<RecipeDto, Recipe> {
    override fun mapToDomainModel(entity: RecipeDto) = Recipe(
        id = entity.pk,
        title =
        entity.title,
        publisher = entity.publisher,
        imageUrl = entity.featuredImage,
        rating = entity.rating,
        sourceUrl = entity.sourceUrl,
        description = entity.description,
        instructions = entity.cookingInstructions,
        ingredients = entity.ingredients,
        dateAdded = entity.dateAdded,
        dateUpdated = entity.dateUpdated
    )

    override fun mapFromDomainModel(domainModel: Recipe) = RecipeDto(
        pk = domainModel.id,
        title = domainModel.title,
        publisher = domainModel.publisher,
        featuredImage = domainModel.imageUrl,
        rating = domainModel.rating,
        sourceUrl = domainModel.sourceUrl,
        description = domainModel.description,
        cookingInstructions = domainModel.instructions,
        ingredients = domainModel.ingredients,
        dateAdded = domainModel.dateAdded,
        dateUpdated = domainModel.dateUpdated
    )

    fun fromDomainList(domainModelList: List<Recipe>) = domainModelList.map { recipe ->
        mapFromDomainModel(recipe)
    }

    fun toDomainList(entityList: List<RecipeDto>) = entityList.map { entity ->
        mapToDomainModel(entity)
    }

}