package com.example.simplemoviesapp.model.data_classes.movies_list_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "moviesListResponse", primaryKeys = ["genre", "page"])
data class MoviesListResponse (
    @ColumnInfo(name = "genre"             ) @SerializedName("genre"         ) var genre        : String             = "ALL",
    @ColumnInfo(name = "page"              ) @SerializedName("page"          ) var page         : Int                = 1,
    @ColumnInfo(name = "results"           ) @SerializedName("results"       ) var results      : ArrayList<Movie> = arrayListOf(),
    @ColumnInfo(name = "total_pages"       ) @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
    @ColumnInfo(name = "total_results"     ) @SerializedName("total_results" ) var totalResults : Int?               = null

)