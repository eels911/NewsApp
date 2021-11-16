package com.example.android.newsapp.repositary

import com.example.android.newsapp.api.RetrofitInstance

class NewsRepository {
    suspend fun getNewsFeed(countryCode: String,pageNumber: Int)=
        RetrofitInstance.api.getNewsFeed(countryCode,pageNumber)

}
