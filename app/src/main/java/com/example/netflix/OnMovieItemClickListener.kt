package com.example.netflix

import com.example.netflix.models.MovieList

interface OnMovieItemClickListener{
    fun onItemClicked(item: MovieList.Result)
}