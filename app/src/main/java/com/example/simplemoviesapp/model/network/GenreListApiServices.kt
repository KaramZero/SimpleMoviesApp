package com.example.simplemoviesapp.model.network


import com.example.simplemoviesapp.model.data_classes.genre_response.GenreResponse
import retrofit2.http.*

interface GenreListApiServices {

    @GET(NetworkKeys.genreListEndPoint)
    suspend fun getGenreList(
        @Query("language", encoded = true) language: String,
        @HeaderMap headers: Map<String, String>
    ): GenreResponse


}