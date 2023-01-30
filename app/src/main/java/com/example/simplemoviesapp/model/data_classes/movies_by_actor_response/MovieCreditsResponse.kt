package com.example.simplemoviesapp.model.data_classes.movies_by_actor_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.google.gson.annotations.SerializedName

@Entity(tableName = "MovieCreditsResponse", primaryKeys = ["id"])
data class MoviesByActorResponse (

  @ColumnInfo(name = "id"               ) @SerializedName("id"   ) var id     : Long             = 0,
  @ColumnInfo(name = "cast"             ) @SerializedName("cast" ) var movies : ArrayList<Movie> = arrayListOf()
)