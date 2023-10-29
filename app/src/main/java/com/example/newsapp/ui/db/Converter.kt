package com.example.newsapp.ui.db

import androidx.room.TypeConverter
import com.example.newsapp.ui.Source

class Converter {
    @TypeConverter
    fun fromSource(source: Source):String{
     return source.name
    }
    @TypeConverter
    fun  toSource(name:String): Source {
        return Source(name,name)
    }
}