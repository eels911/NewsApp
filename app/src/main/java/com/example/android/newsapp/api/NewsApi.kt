package com.example.android.newsapp.api

import com.example.android.newsapp.models.NewsApiResponse
import com.example.android.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
@GET("top-headlines")
suspend fun getNewsFeed(
    @Query("country")
    countryCode: String = "us",
    @Query("page")
    pageNumber: Int = 1,
    @Query("apiKey")
    apiKey: String = API_KEY
): Response<NewsApiResponse>
}