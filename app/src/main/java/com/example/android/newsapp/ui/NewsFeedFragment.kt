package com.example.android.newsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.example.android.newsapp.R
import com.example.android.newsapp.util.Resource

class NewsFeedFragment:Fragment(R.layout.news_feed_fragment) {


    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter

    lateinit var rvNewsFeed: RecyclerView

    lateinit var paginationProgressBar: ProgressBar

    val TAG = "NewsFeedFragment"

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            paginationProgressBar = view.findViewById(R.id.paginationProgressBar)
            rvNewsFeed =  view.findViewById(R.id.rvNewsFeed)
            viewModel = (activity as MainActivity).viewModel
            setupRecyclerView()
            viewModel.newsFeed.observe(viewLifecycleOwner, Observer { response->
                when(response){
                    is Resource.Success ->{
                        hideProgressBar()
                        response.data?.let { newsApiResponse ->
                            newsAdapter.differ.submitList(newsApiResponse.articles)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            Log.e(TAG,"An error occured: $message")
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }

            })
              }
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE

    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
            rvNewsFeed.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter=newsAdapter
            }
    }

    }
