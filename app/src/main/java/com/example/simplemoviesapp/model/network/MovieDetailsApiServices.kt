package com.example.simplemoviesapp.model.network


import com.example.simplemoviesapp.model.data_classes.movie_credits_response.MovieCreditsResponse
import com.example.simplemoviesapp.model.data_classes.movie_images_response.MovieImagesResponse
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import retrofit2.http.*

interface MovieDetailsApiServices {

    @GET("${NetworkKeys.BASE_URL}movie/{movieID}")
    suspend fun getMovieDetails(
        @Path("movieID") movieID: String,
        @Query("language", encoded = true) language: String,
        @HeaderMap headers: Map<String, String>
    ): Movie


    @GET("${NetworkKeys.BASE_URL}movie/{movieID}/credits")
    suspend fun getMovieCast(
        @Path("movieID") movieID: String,
        @Query("language", encoded = true) language: String,
        @HeaderMap headers: Map<String, String>
    ): MovieCreditsResponse

    @GET("${NetworkKeys.BASE_URL}movie/{movieID}/images")
    suspend fun getMovieImages(
        @Path("movieID") movieID: String,
        @HeaderMap headers: Map<String, String>
    ): MovieImagesResponse


}