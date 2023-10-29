package com.example.newsapp.ui.NEWAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.ui.Article
import com.example.newsapp.databinding.ItemArticleBinding

class newAdapter:RecyclerView.Adapter<newAdapter.NewViewHolder>() {
    inner class NewViewHolder(itemView:ItemArticleBinding):RecyclerView.ViewHolder(itemView.root){
        val binding =itemView


    }
    private val differCallback=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
          return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
  return NewViewHolder(ItemArticleBinding.inflate(LayoutInflater.
              from(parent.context),parent,false)
  )
    }

    override fun getItemCount(): Int {
      return differ.currentList.size
    }
    private var onItemClickListener: ((Article) -> Unit)? = null


    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        var article=differ.currentList[position]
        holder.itemView.also {
            Glide.with(it.context.applicationContext).load(article.urlToImage).into(holder.binding.ivArticleImage)
            holder.binding.tvSource.text=article.source.name
            holder.binding.tvTitle.text=article.title
            holder.binding.tvPublishedAt.text=article.publishedAt
            holder.binding.tvDescription.text=article.description
            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(article) }
            }
        }


    }
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener=listener
    }
}