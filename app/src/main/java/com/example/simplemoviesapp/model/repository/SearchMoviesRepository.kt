package com.example.simplemoviesapp.model.repository


import com.example.simplemoviesapp.model.data_classes.movies_list_response.MoviesListResponse
import com.example.simplemoviesapp.model.network.NetworkKeys
import com.example.simplemoviesapp.model.network.SearchMoviesApiServices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMoviesRepository @Inject constructor(private var api: SearchMoviesApiServices) {

    /**
     * it takes the query String and page number to get the Movies List with.
     *
     * tries to get the Movie List from API.
     *
     * throws Exception caused by the network if couldn't fitch the Movie List.
     */
    suspend fun searchForMovies(query: String = "", page: Int = 1): MoviesListResponse {
        return api.searchForMovies(query = query, page,NetworkKeys.language, NetworkKeys.headers)
    }
}