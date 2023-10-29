package com.example.newsapp.ui.db

import android.content.Context
import androidx.room.*
import com.example.newsapp.ui.Article

@Database( entities = [Article::class], version = 1)
@TypeConverters(Converter::class)
abstract class AritcleDatabase :RoomDatabase(){
 abstract fun getArticledao():ArticleDao
 companion object{
  @Volatile
  private var instance:AritcleDatabase?=null
  private val LOCK=Any()
  operator fun invoke(context: Context)= instance?: synchronized(LOCK){
   instance?:createDatabase(context).also{ instance = it}
  }
  private fun createDatabase(context: Context)= Room.databaseBuilder(context.applicationContext,AritcleDatabase::class.java,
  "article_db.db").build()
 }
}