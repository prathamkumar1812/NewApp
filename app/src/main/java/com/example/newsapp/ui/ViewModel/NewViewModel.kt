package com.example.newsapp.ui.ViewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.ui.Article
import com.example.newsapp.ui.NewsApplication
import com.example.newsapp.ui.Repository.NewRepository
import com.example.newsapp.ui.Resource
import com.example.newsapp.ui.newResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewViewModel(app:Application,val repository: NewRepository): AndroidViewModel(app) {

    val breakingNew:MutableLiveData<Resource<newResponce>> = MutableLiveData()
    var  breakingnew_Page=1

    var breakingNewRespomse:newResponce?=null
    val SearchNew:MutableLiveData<Resource<newResponce>> = MutableLiveData()
    var  Serach_Page=1
    var SearchNewRespomse:newResponce?=null
    init {
        getBreakingNew("in")
    }
    fun getBreakingNew(countrycode:String)=GlobalScope.launch {
        safeBreakingNewsCall(countrycode)
    }
    fun getsearchNew(countrycode:String)=GlobalScope.launch {
      safeSearchNewsCall(countrycode)
    }
private fun HandlebreakingnewResponse(response:Response<newResponce>):Resource<newResponce>{
    if(response.isSuccessful){
        response.body()?.let{
            breakingnew_Page++
            if(breakingNewRespomse==null){
                breakingNewRespomse=it
            }
            else{
                val oldearticle=breakingNewRespomse?.articles
                val newArticle=it.articles
                oldearticle?.addAll(newArticle)
            }
        return Resource.Success(breakingNewRespomse?:it)
        }
    }
    return Resource.Error(response.message())
}
    private fun HandleSearchnewResponse(response:Response<newResponce>):Resource<newResponce>{

        if(response.isSuccessful){
            response.body()?.let{
                Serach_Page++
                if(SearchNewRespomse==null){
                    SearchNewRespomse=it
                }
                else{
                    val oldearticle=SearchNewRespomse?.articles
                    val newArticle=it.articles
                    oldearticle?.addAll(newArticle)
                }
                return Resource.Success(SearchNewRespomse?:it)
            }
        }
        return Resource.Error(response.message())
    }

   fun savearticle(article: Article)=GlobalScope.launch {
        repository.upsert(article)
    }
    fun deletearticle(article: Article)=GlobalScope.launch {
        repository.delete(article)
    }
    fun getartcile()=repository.getsavedarticle()

    private suspend fun safeSearchNewsCall(searchQuery: String) {
      //  newSearchQuery = searchQuery
        SearchNew.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.newsearch(searchQuery, Serach_Page)
                SearchNew.postValue(HandleSearchnewResponse(response))
            } else {
                SearchNew.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> SearchNew.postValue(Resource.Error("Network Failure"))
                else -> SearchNew.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNew.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.getbreakingNew(countryCode, breakingnew_Page)
                breakingNew.postValue(HandlebreakingnewResponse(response))
            } else {
                breakingNew.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> breakingNew.postValue(Resource.Error("Network Failure"))
                else -> breakingNew.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false

    }

}
