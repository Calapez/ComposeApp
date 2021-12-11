package com.example.composeapp.di

import com.example.composeapp.network.IRecipeService
import com.example.composeapp.network.model.RecipeDtoMapper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRecipeService() : IRecipeService {
        return Retrofit.Builder()
            .baseUrl("https://food2fork.ca/api/recipe/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(IRecipeService::class.java)
    }

    @Singleton
    @Provides
    fun provideRecipeMapper() : RecipeDtoMapper {
        return RecipeDtoMapper()
    }

    @Singleton
    @Provides
    fun provideAuthToken() : String = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"

}