package com.example.newsapp.ui.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.newsapp.R
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.NEWAdapter.newAdapter
import com.example.newsapp.ui.Resource
import com.example.newsapp.ui.ViewModel.NewViewModel
import com.example.newsapp.ui.util.Constant.Companion.QUERY_PAGE_SIZE
import retrofit2.Response


class BreakingNewFragement : Fragment(R.layout.fragment_breaking_new_fragement) {
    lateinit var progressBar: ProgressBar
    lateinit var newAdapter: newAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var viewModel:NewViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView=view.findViewById(R.id.rvBreakingNews)
        progressBar=view.findViewById(R.id.paginationProgressBar)
        super.onViewCreated(view, savedInstanceState)


        viewModel=(activity as MainActivity).ViewModle
        setup()

        newAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewFragement_to_articalFragment,
                bundle
            )}
        viewModel.breakingNew.observe(viewLifecycleOwner, Observer {responce->
            when(responce){
                is Resource.Success ->{
                    hideProgerssbar()
                    responce.data?.let {
                        newAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingnew_Page == totalPages
                    }
                }

                is Resource.Error -> {
                    hideProgerssbar()
                    responce.message?.let {
                      Toast.makeText(activity,"An Error Occured",Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading->{
                    showProgerssbar()
                }
            }
        })
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
                viewModel.getBreakingNew("in")
                isScrolling = false
            } else {
                recyclerView.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
    fun setup(){
        newAdapter= newAdapter()
        recyclerView.apply {
            adapter=newAdapter
            layoutManager=LinearLayoutManager(activity)
                    addOnScrollListener(this@BreakingNewFragement.scrollListener)
        }
    }
    fun hideProgerssbar(){
        progressBar.visibility=View.GONE
        isLoading=false

    }
    fun showProgerssbar(){
        progressBar.visibility=View.VISIBLE
        isLoading=true

    }

}