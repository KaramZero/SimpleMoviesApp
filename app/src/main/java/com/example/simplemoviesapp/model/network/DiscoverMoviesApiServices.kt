package com.example.simplemoviesapp.model.network


import com.example.simplemoviesapp.model.data_classes.movies_list_response.MoviesListResponse
import retrofit2.http.*

interface DiscoverMoviesApiServices {

    @GET(NetworkKeys.discoverMoviesEndPoint)
    suspend fun getMovies(
        @Query("with_genres", encoded = true) genres: String,
        @Query("page", encoded = true) page: Int,
        @Query("language", encoded = true) language: String,
        @HeaderMap headers: Map<String, String>
    ): MoviesListResponse

}