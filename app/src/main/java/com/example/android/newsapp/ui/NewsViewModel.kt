package com.example.android.newsapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.newsapp.models.NewsApiResponse
import com.example.android.newsapp.repositary.NewsRepository
import com.example.android.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository,
//                    app: Application
): ViewModel() {
    val newsFeed: MutableLiveData<Resource<NewsApiResponse>> = MutableLiveData()

    var newsFeedPage = 1

    init {
        getNewsFeed("us")
    }

    fun getNewsFeed(countryCode: String) = viewModelScope.launch {
        newsFeed.postValue(Resource.Loading())
        val response = newsRepository.getNewsFeed(countryCode, newsFeedPage)
        newsFeed.postValue(handleNewsFeedResponse(response))
    }
    private fun handleNewsFeedResponse(response: Response<NewsApiResponse>) : Resource<NewsApiResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}