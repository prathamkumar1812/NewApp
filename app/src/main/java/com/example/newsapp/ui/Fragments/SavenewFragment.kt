package com.example.newsapp.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.NEWAdapter.newAdapter

import com.example.newsapp.ui.ViewModel.NewViewModel
import com.google.android.material.snackbar.Snackbar


class SavenewFragment : Fragment(R.layout.fragment_savenew) {
    lateinit var newAdapter:newAdapter
    lateinit var viewModel: NewViewModel
    lateinit var recyclerView:RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).ViewModle
        recyclerView=view.findViewById(R.id.rvSavedNews)
        newAdapter=newAdapter()
       setup()
        newAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savenewFragment_to_articalFragment,
                bundle
            )}
        val itemTouchHelperCallback=object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT 
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val postion=viewHolder.adapterPosition
                val article=newAdapter.differ.currentList[postion]
                viewModel.deletearticle(article)
                Snackbar.make(view,"Succesfully Delete Article",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.savearticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)
        }



        viewModel.getartcile().observe(viewLifecycleOwner, Observer {
            newAdapter.differ.submitList(it)
        })
    }
    fun setup(){
        newAdapter= newAdapter()
        recyclerView.apply {
            adapter=newAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }}
