package com.example.simplemoviesapp.model.data_classes.movies_list_response

import com.google.gson.annotations.SerializedName

data class MoviesListResponse (
    @SerializedName("genre"         ) var genre        : String             = "ALL",
    @SerializedName("page"          ) var page         : Int                = 1,
    @SerializedName("results"       ) var results      : ArrayList<Movie> = arrayListOf(),
    @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
    @SerializedName("total_results" ) var totalResults : Int?               = null

)