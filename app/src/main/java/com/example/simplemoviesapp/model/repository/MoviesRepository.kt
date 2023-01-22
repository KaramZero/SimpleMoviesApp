package com.example.simplemoviesapp.model.repository


import com.example.simplemoviesapp.model.data_classes.movies_list_response.MoviesListResponse
import com.example.simplemoviesapp.model.local_source.MoviesDao
import com.example.simplemoviesapp.model.network.DiscoverMoviesApiServices
import com.example.simplemoviesapp.model.network.NetworkKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private var api: DiscoverMoviesApiServices,
    private var moviesDao: MoviesDao
) {

    /**
     * it takes the genres Id and page number to get the Movies List with.
     *
     * tries to get the Movie List from API, and cache it to the DataBase
     * if couldn't, it tries to fitch last cached one from DataBase.
     *
     * throws NullPointerException if couldn't fitch the Movie List.
     */
    suspend fun getMovies(genres: String = "", page: Int = 1): MoviesListResponse {

        var res: MoviesListResponse?
        try {
            res = api.getMovies(genres, page, NetworkKeys.language, NetworkKeys.headers)
            res.genre = genres.ifEmpty { "ALL" }
            insertMoviesListResponse(res)
        } catch (ex: Exception) {
            res = getMoviesFromDB(genres.ifEmpty { "ALL" }, page)
        }

        if (res == null)
            throw NullPointerException("can't get the MoviesList from API, and the MoviesList couldn't be found in DataBases")

        return res
    }


    /**
     * cache the Movies List to the DataBases.
     */
    private fun insertMoviesListResponse(movies: MoviesListResponse) {
        Thread {
            moviesDao.insertMoviesListResponse(movies)
        }.start()
    }

    /**
     * it takes the genres Id and page number to get the Movies List with..
     *
     * get the Movies List from the DataBases.
     */
    private fun getMoviesFromDB(genres: String, page: Int): MoviesListResponse {
        return moviesDao.getMovies(genre = genres, page = page)
    }
}