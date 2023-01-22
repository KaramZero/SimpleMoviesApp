package com.example.simplemoviesapp.model.data_classes.movie_credits_response

import com.google.gson.annotations.SerializedName


data class Cast (

  @SerializedName("id"                   ) var id                 : Int?     = null,
  @SerializedName("name"                 ) var name               : String?  = null,
  @SerializedName("profile_path"         ) var profilePath        : String?  = null,

)