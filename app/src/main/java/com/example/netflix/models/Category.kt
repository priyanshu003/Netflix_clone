package com.example.netflix.models

data class Category(
    val movieHeading: String = "TOP RATED",
    val category: ArrayList<MovieList.Result> = ArrayList()
)
