package com.example.newsapp.ui.api

import com.example.newsapp.ui.newResponce
import com.example.newsapp.ui.util.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewApi {

    @GET("v2/top-headlines")
    suspend fun getbreakingnew(
        @Query("country")
        countryCode:String="in",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<newResponce>


    @GET("v2/everything")
    suspend fun Searchresult(
        @Query("q")
        Searchquery:String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<newResponce>
}