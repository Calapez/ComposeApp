package com.example.composeapp.util

import com.example.composeapp.domain.model.Recipe

object MockRecipes {

    val mockRecipe = Recipe(
        1,
        "Happy Meal",
        "Publisher",
        "https://pt.wikipedia.org/wiki/Scarlett_Johansson#/media/Ficheiro:Scarlett_Johansson_by_Gage_Skidmore_2_(cropped).jpg",
        3,
        "",
        "Description",
        "1,2,3",
        listOf("Burger", "lettuce"))

    val mockRecipeList = listOf(
        Recipe(
            1,
            "Happy Meal",
            "Publisher",
            "https://pt.wikipedia.org/wiki/Scarlett_Johansson#/media/Ficheiro:Scarlett_Johansson_by_Gage_Skidmore_2_(cropped).jpg",
            3,
            "",
            "Description",
            "1,2,3",
            listOf("Burger", "lettuce")),
        Recipe(
            2,
            "McChicken",
            "Publisher",
            "https://i.pinimg.com/736x/7c/e3/ea/7ce3ea124beb528e57a9d956df0c00e0--mcdonalds-canada.jpg",
            3,
            "",
            "Description",
            "1,2,3",
            listOf("Burger", "lettuce")),
        Recipe(
            3,
            "Big Mac",
            "Publisher",
            "https://t8j5n5j3.rocketcdn.me/wp-content/uploads/2015/09/o-que-um-big-mac-faz-com-seu-corpo-depois-de-1-hora.jpg",
            1,
            "",
            "Description",
            "1,2,3",
            listOf("Burger", "lettuce")),
    )

}