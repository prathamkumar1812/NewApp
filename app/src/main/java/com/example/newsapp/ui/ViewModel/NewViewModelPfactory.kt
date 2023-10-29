package com.example.newsapp.ui.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.ui.Repository.NewRepository

class NewViewModelPfactory(val app:Application,val repository: NewRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  NewViewModel(app,repository) as T
    }
}