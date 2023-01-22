package com.example.simplemoviesapp.model.repository


import com.example.example.Posters
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.Cast
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.MovieCreditsResponse
import com.example.simplemoviesapp.model.data_classes.movie_images_response.MovieImagesResponse
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.local_source.MoviesDetailsDao
import com.example.simplemoviesapp.model.network.MovieDetailsApiServices
import com.example.simplemoviesapp.model.network.NetworkKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsRepository @Inject constructor(
    private var api: MovieDetailsApiServices,
    private var dao: MoviesDetailsDao
) {

    /**
     * it takes the movie Id to get the Movie with.
     *
     * tries to get the Movie Details from API, and cache it to the DataBase
     * if couldn't, it tries to fitch last cached one from DataBase.
     *
     * throws NullPointerException if couldn't fitch the Movie Details.
     */
    suspend fun getMovieDetails(movieId: String): Movie {

        var res: Movie?
        try {
            res = api.getMovieDetails(movieId, NetworkKeys.language, NetworkKeys.headers)
            insertMovie(res)
        } catch (ex: Exception) {
            res = getMovieFromDB(id = movieId)
        }

        if (res == null)
            throw NullPointerException("can't get the movie from API, and the movie couldn't be found in DataBases")

        return res
    }


    /**
     * it takes the movie Id to get the Movie with.
     *
     * tries to get the Movie Cast from API, and cache it to the DataBase
     * if couldn't, it tries to fitch last cached one from DataBase.
     *
     * throws NullPointerException if couldn't fitch the Movie Cast.
     */
    suspend fun getMovieCast(movieId: String): ArrayList<Cast> {
        var res: MovieCreditsResponse?
        try {
            res = api.getMovieCast(movieId, NetworkKeys.language, NetworkKeys.headers)
            insertMovieCast(res)
        } catch (ex: Exception) {
            res = getMovieCastFromDB(id = movieId)
        }

        if (res == null)
            throw NullPointerException("can't get the MovieCast from API, and the MovieCast couldn't be found in DataBases")

        return res.cast
    }


    /**
     * it takes the movie Id to get the Movie with.
     *
     * tries to get the Movie Images from API, and cache it to the DataBase
     * if couldn't, it tries to fitch last cached one from DataBase.
     *
     * throws NullPointerException if couldn't fitch the Movie Images.
     */
    suspend fun getMovieImages(movieId: String): ArrayList<Posters> {
        var res: MovieImagesResponse?
        try {
            res = api.getMovieImages(movieId, NetworkKeys.headers)
            insertMovieImages(res)
        } catch (ex: Exception) {
            res = getMovieImagesFromDB(id = movieId)
        }

        if (res == null)
            throw NullPointerException("can't get the MovieImages from API, and the MovieImages couldn't be found in DataBases")

        return res.posters
    }



    /**
     * cache the Movie to the DataBases.
     */
    private fun insertMovie(movie: Movie) {
        Thread {
            dao.insertMovie(movie)
        }.start()
    }

    /**
     * it takes the Movie Id to get the movie details with.
     *
     * get the Movie Details from the DataBases.
     */
    private fun getMovieFromDB(id: String): Movie {
        return dao.getMovieDetails(id = id)
    }


    /**
     * cache the Movie cast to the DataBases.
     */
    private fun insertMovieCast(cast: MovieCreditsResponse) {
        Thread {
            dao.insertMovieCast(cast)
        }.start()
    }

    /**
     * it takes the Movie Id to get the cast with.
     *
     * get the Movie cast from the DataBases.
     */
    private fun getMovieCastFromDB(id: String): MovieCreditsResponse {
        return dao.getMovieCast(id = id)
    }


    /**
     * cache the Movie images to the DataBases.
     */
    private fun insertMovieImages(images: MovieImagesResponse) {
        Thread {
            dao.insertMovieImages(images)
        }.start()
    }

    /**
     * it takes the Movie Id to get the images with.
     *
     * get the Movie images from the DataBases.
     */
    private fun getMovieImagesFromDB(id: String): MovieImagesResponse {
        return dao.getMovieImages(id = id)
    }

}