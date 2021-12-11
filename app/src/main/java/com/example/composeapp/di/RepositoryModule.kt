package com.example.composeapp.di

import androidx.compose.runtime.Composable
import com.example.composeapp.network.IRecipeService
import com.example.composeapp.network.model.RecipeDtoMapper
import com.example.composeapp.repository.IRecipeRepository
import com.example.composeapp.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeService: IRecipeService,
        recipeDtoMapper: RecipeDtoMapper
    ) : IRecipeRepository {
        return RecipeRepository(recipeService, recipeDtoMapper)
    }
}