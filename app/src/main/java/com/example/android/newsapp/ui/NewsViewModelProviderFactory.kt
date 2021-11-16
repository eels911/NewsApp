package com.example.android.newsapp.ui;

import android.app.Application;
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.android.newsapp.repositary.NewsRepository;

class NewsViewModelProviderFactory(
//        val app:Application,
        val newsRepository:NewsRepository
) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
        }
        }