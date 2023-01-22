package com.example.simplemoviesapp.model.network

import java.util.*
import kotlin.collections.HashMap


object NetworkKeys {

    const val BASE_URL: String = "https://api.themoviedb.org/3/"

    const val IMAGES_BASE_URL: String = "https://image.tmdb.org/t/p/w500"
    const val discoverMoviesEndPoint = "discover/movie"
    const val searchMoviesEndPoint = "search/movie"
    const val genreListEndPoint = "genre/movie/list"

    var language: String = Locale.getDefault().language

    var AuthorizationToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmY2E4YzE0MzcwYzlhZTQyMjM4NzFjYWQwOWY1Nzg5ZCIsInN1YiI6IjYzYzk5MzA2MDA2YjAxMDA3YjkyMGM4NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._8aDhQH_FJBOtREYxwudwJU3CittVdI_dvzgJwZl_RQ"

    private const val Content_Type = "application/json"

    var headers: Map<String, String> = headerMap()

    private fun headerMap(): HashMap<String, String> {
        val header: HashMap<String, String> = HashMap()
        header["Content-Type"] = Content_Type
        header["Accept"] = Content_Type
        header["Authorization"] = AuthorizationToken
        return header
    }


}