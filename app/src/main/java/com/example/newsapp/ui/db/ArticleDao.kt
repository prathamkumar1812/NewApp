package com.example.newsapp.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.ui.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long
    @Query("Select * From article")
    fun getAllartllicle():LiveData<List<Article>>
    @Delete
    suspend fun Delete(article: Article)
}