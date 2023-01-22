package com.example.simplemoviesapp.model.repository


import com.example.simplemoviesapp.model.data_classes.genre_response.Genre
import com.example.simplemoviesapp.model.data_classes.genre_response.GenreResponse
import com.example.simplemoviesapp.model.local_source.GenresDao
import com.example.simplemoviesapp.model.network.GenreListApiServices
import com.example.simplemoviesapp.model.network.NetworkKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private var api: GenreListApiServices,
    private var genresDao: GenresDao
) {

    /**
     * get the Genre List from API, and cache it to the DataBase
     * if couldn't, it tries to fitch last cached list from DataBase.
     *
     * throws NullPointerException if couldn't fitch the List.
     */
    suspend fun getGenreList(): ArrayList<Genre> {

        var res: GenreResponse?
        try {
            res = api.getGenreList(NetworkKeys.language, NetworkKeys.headers)
            insertGenreResponse(res)
        } catch (ex: Exception) {
            res = getGenreResponse()
        }

        if (res == null)
            throw NullPointerException("can't get the GenreList from API, and the GenreList couldn't be found in DataBases")

        return res.genres
    }

    /**
     * cache the Genre List to the DataBases.
     */
    private fun insertGenreResponse(genres: GenreResponse) {
        Thread {
            genresDao.insertGenreResponse(genres)
        }.start()
    }


    /**
     * get the Genre List from the DataBases.
     */
    private fun getGenreResponse(): GenreResponse {
        return genresDao.getGenreResponse()
    }

}