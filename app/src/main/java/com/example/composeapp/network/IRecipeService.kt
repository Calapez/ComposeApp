package com.example.composeapp.network

import com.example.composeapp.network.model.RecipeDto
import com.example.composeapp.network.responses.RecipeSearchResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IRecipeService {

    @GET("search")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ) : RecipeSearchResponse

    @GET("get")
    suspend fun get(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ) : RecipeDto

}