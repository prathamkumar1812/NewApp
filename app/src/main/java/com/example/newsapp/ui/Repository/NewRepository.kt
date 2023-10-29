package com.example.newsapp.ui.Repository

import com.example.newsapp.ui.Article
import com.example.newsapp.ui.api.Retrofitinstance
import com.example.newsapp.ui.db.AritcleDatabase

class NewRepository(val db:AritcleDatabase) {
    suspend fun getbreakingNew(country:String,page:Int)=
        Retrofitinstance.api.getbreakingnew(country,page)

   suspend fun newsearch( searchQuery:String,page:Int)=
       Retrofitinstance.api.Searchresult(searchQuery,page)


    suspend fun upsert(article: Article)=db.getArticledao().upsert(article)
    fun getsavedarticle()=db.getArticledao().getAllartllicle()
    suspend fun delete(article: Article)=db.getArticledao().Delete(article)
}