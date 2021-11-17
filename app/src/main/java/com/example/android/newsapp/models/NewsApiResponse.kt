package com.example.android.newsapp.models

data class NewsApiResponse (
    val status: String,
    val totalResults: Int,
    val articles: MutableList<NewsHeadlines>

)