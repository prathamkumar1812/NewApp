package com.example.newsapp.ui.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.NEWAdapter.newAdapter
import com.example.newsapp.ui.Resource
import com.example.newsapp.ui.ViewModel.NewViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchnewFragment : Fragment(R.layout.fragment_searchnew) {
    lateinit var search:EditText
    lateinit var viewModel: NewViewModel
    lateinit var progressBar: ProgressBar
    lateinit var newAdapter: newAdapter
    lateinit var recyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView=view.findViewById(R.id.rvSearchNews)
        progressBar=view.findViewById(R.id.paginationProgressBar)
        search=view.findViewById(R.id.etSearch)
        viewModel=(activity as MainActivity).ViewModle
        setup()
        newAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchnewFragment_to_articalFragment,
                bundle
            )}
        var job: Job? = null
        search.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.getsearchNew(editable.toString())
                    }
                }
            }
        }
        viewModel.SearchNew.observe(viewLifecycleOwner, Observer {responce->
            when(responce){
                is Resource.Success ->{
                    hideProgerssbar()
                    responce.data?.let {
                        newAdapter.differ.submitList(it.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgerssbar()
                    responce.message?.let {
                        Toast.makeText(activity,"An Error Occured", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading->{
                    showProgerssbar()
                }
            }
        })
    }
    fun setup(){
        newAdapter= newAdapter()
        recyclerView.apply {
            adapter=newAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
    fun hideProgerssbar(){
        progressBar.visibility=View.GONE

    }
    fun showProgerssbar(){
        progressBar.visibility=View.VISIBLE

    }
    }
