package com.example.simplemoviesapp.model.data_classes.movies_list_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies", primaryKeys = ["id"])
data class Movie (

  @ColumnInfo(name = "backdrop_path"    ) @SerializedName("backdrop_path"     ) var backdropPath     : String?        = null,
  @ColumnInfo(name = "id"               ) @SerializedName("id"                ) var id               : Int            = 0,
  @ColumnInfo(name = "original_title"   ) @SerializedName("original_title"    ) var originalTitle    : String?        = null,
  @ColumnInfo(name = "overview"         ) @SerializedName("overview"          ) var overview         : String?        = null,
  @ColumnInfo(name = "poster_path"      ) @SerializedName("poster_path"       ) var posterPath       : String?        = null,
  @ColumnInfo(name = "release_date"     ) @SerializedName("release_date"      ) var releaseDate      : String?        = null,
  @ColumnInfo(name = "title"            ) @SerializedName("title"             ) var title            : String?        = null,
  @ColumnInfo(name = "vote_average"     ) @SerializedName("vote_average"      ) var voteAverage      : Double?        = null,
  @ColumnInfo(name = "genre_ids"        ) @SerializedName("genre_ids"         ) var genreIds         : ArrayList<Int> = arrayListOf()
  )