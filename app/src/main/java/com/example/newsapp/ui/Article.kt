package com.example.newsapp.ui

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity( tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    var author: String="",
    var content: String="",
    var description: String="",
    val publishedAt: String="",
    val source: Source= Source("",""),
    val title: String="",
    val url: String="",
    var urlToImage: String=""
): Serializable{

    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url.isNullOrEmpty()){
            result = 31 * result + url.hashCode()
        }
        return result
    }
}