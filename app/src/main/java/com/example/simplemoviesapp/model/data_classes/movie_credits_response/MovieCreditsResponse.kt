package com.example.simplemoviesapp.model.data_classes.movie_credits_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "MovieCreditsResponse", primaryKeys = ["id"])
data class MovieCreditsResponse (

  @ColumnInfo(name = "id"               ) @SerializedName("id"   ) var id   : Int             = 0,
  @ColumnInfo(name = "cast"             ) @SerializedName("cast" ) var cast : ArrayList<Cast> = arrayListOf()
)