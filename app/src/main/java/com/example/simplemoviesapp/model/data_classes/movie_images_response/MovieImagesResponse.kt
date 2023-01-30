package com.example.simplemoviesapp.model.data_classes.movie_images_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.example.Posters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "MovieImagesResponse", primaryKeys = ["id"])
data class MovieImagesResponse (

  @ColumnInfo(name = "id"               ) @SerializedName("id"        ) var id        : Long                 = 0,
  @ColumnInfo(name = "posters"          ) @SerializedName("posters"   ) var posters   : ArrayList<Posters>   = arrayListOf()

)