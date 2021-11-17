package com.example.android.newsapp.models
import java.io.Serializable
data class NewsHeadlines (
    val source: Source? = null,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage:String?,
    val publishedAt: String,
    val content: String
        ): Serializable