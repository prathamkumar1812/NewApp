package com.example.newsapp.ui

data class newResponce(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)