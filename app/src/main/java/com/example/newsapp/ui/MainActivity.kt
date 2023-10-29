package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.ui.Repository.NewRepository
import com.example.newsapp.ui.ViewModel.NewViewModel
import com.example.newsapp.ui.ViewModel.NewViewModelPfactory
import com.example.newsapp.ui.db.AritcleDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
   lateinit var ViewModle:NewViewModel
  lateinit var binding: ActivityMainBinding
  //  lateinit var frgaemnt:Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newNavHostFragment) as NavHostFragment

        val repository=NewRepository(AritcleDatabase(this))
        ViewModle=ViewModelProvider(this,NewViewModelPfactory(application,repository)).get(NewViewModel::class.java)
       binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())


    }
}