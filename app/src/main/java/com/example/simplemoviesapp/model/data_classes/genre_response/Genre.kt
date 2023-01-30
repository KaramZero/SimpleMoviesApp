package com.example.simplemoviesapp.model.data_classes.genre_response

import com.google.gson.annotations.SerializedName


data class Genre (

    @SerializedName("id"   ) var id   : Long?   = null,
    @SerializedName("name" ) var name : String? = null

)