package com.example.android.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.newsapp.NewsApplication
import com.example.android.newsapp.models.NewsApiResponse
import com.example.android.newsapp.repositary.NewsRepository
import com.example.android.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(private val newsRepository: NewsRepository,
                   app: Application
): AndroidViewModel(app) {
    val newsFeed: MutableLiveData<Resource<NewsApiResponse>> = MutableLiveData()

    var newsFeedPage = 1

    var newsFeedResponse: NewsApiResponse? = null

    init {
        getNewsFeed("us")
    }

    fun getNewsFeed(countryCode: String) = viewModelScope.launch {
        newsFeed.postValue(Resource.Loading())
//        val response = newsRepository.getNewsFeed(countryCode, newsFeedPage)
//        newsFeed.postValue(handleNewsFeedResponse(response))
        safeBreakingNewsCall(countryCode)
    }
    private suspend fun safeBreakingNewsCall(countryCode: String) {
        newsFeed.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepository.getNewsFeed(countryCode, newsFeedPage)
                newsFeed.postValue(handleNewsFeedResponse(response))
            } else {
                newsFeed.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> newsFeed.postValue(Resource.Error("Network Failure"))
                else -> newsFeed.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun handleNewsFeedResponse(response: Response<NewsApiResponse>) : Resource<NewsApiResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                newsFeedPage++
                if(newsFeedResponse == null){
                    newsFeedResponse = resultResponse
                }else{
                    val oldArticles = newsFeedResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(newsFeedResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}