package com.example.android.newsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.example.android.newsapp.R
import com.example.android.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
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
            newsAdapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putSerializable("newsheadlines",it)
                }
                findNavController().navigate(
                    R.id.action_newsFeedFragment_to_articleFragment,
                bundle
                )
            }
            viewModel.newsFeed.observe(viewLifecycleOwner, Observer { response->
                when(response){
                    is Resource.Success ->{
                        hideProgressBar()
                        response.data?.let { newsApiResponse ->
                            newsAdapter.differ.submitList(newsApiResponse.articles.toList())
                            val totalPages = newsApiResponse.totalResults / QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.newsFeedPage == totalPages
                            if(isLastPage){
                                rvNewsFeed.setPadding(0,0,0,0)
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->

                                Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
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
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true

    }
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getNewsFeed("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
            rvNewsFeed.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter=newsAdapter
                addOnScrollListener(this@NewsFeedFragment.scrollListener)
            }
    }

    }
