package com.example.simplemoviesapp.model.data_classes

import androidx.room.TypeConverter
import com.example.example.Posters
import com.example.simplemoviesapp.model.data_classes.genre_response.Genre
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.Cast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

object Converters {
    @TypeConverter
    fun genresFromString(value: String?): ArrayList<Genre> {
        val listType: Type = object : TypeToken<ArrayList<Genre?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun genresToString(list: ArrayList<Genre?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }


    @TypeConverter
    fun castFromString(value: String?): ArrayList<Cast> {
        val listType: Type = object : TypeToken<ArrayList<Cast?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun castToString(list: ArrayList<Cast?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }




    @TypeConverter
    fun postersFromString(value: String?): ArrayList<Posters> {
        val listType: Type = object : TypeToken<ArrayList<Posters?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun postersToString(list: ArrayList<Posters?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun genresIdsFromString(value: String?): ArrayList<Int> {
        val listType: Type = object : TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun genresIdsToString(list: ArrayList<Int?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}
