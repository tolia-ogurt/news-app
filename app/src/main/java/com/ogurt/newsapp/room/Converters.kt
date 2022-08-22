package com.ogurt.newsapp.room

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromSource(name: String): NewsEntity.Source {
        return NewsEntity.Source(name)
    }

    @TypeConverter
    fun toSource(source: NewsEntity.Source): String {
        return source.name.toString()
    }
}