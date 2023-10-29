package com.example.newsapp.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.ViewModel.NewViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class ArticalFragment : Fragment(R.layout.fragment_artical) {
    lateinit var viewModel: NewViewModel
    lateinit var Webview:WebView
    lateinit var fab:FloatingActionButton
   val argaa:ArticalFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Webview=view.findViewById(R.id.webView)
        fab=view.findViewById(R.id.fab)
        viewModel=(activity as MainActivity).ViewModle
        val article=argaa.article
        Webview.apply {
            webViewClient= WebViewClient()
            val url=article.url.toString()
            loadUrl(url)
        }
        fab.setOnClickListener { v->
            viewModel.savearticle(article)
            Snackbar.make(view,"Artical saved",Snackbar.LENGTH_SHORT).show()
        }
    }
}