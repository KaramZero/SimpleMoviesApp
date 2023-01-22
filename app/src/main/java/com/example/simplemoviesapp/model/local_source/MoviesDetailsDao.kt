package com.example.simplemoviesapp.model.local_source

import androidx.room.*
import com.example.simplemoviesapp.model.data_classes.genre_response.GenreResponse
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.MovieCreditsResponse
import com.example.simplemoviesapp.model.data_classes.movie_images_response.MovieImagesResponse
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.data_classes.movies_list_response.MoviesListResponse

@Dao
interface MoviesDetailsDao {
    @Query("SELECT * From movies WHERE id LIKE :id LIMIT 1")
    fun getMovieDetails(id: String): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie?)


    @Query("SELECT * From MovieCreditsResponse WHERE id LIKE :id LIMIT 1")
    fun getMovieCast(id: String): MovieCreditsResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieCast(cast: MovieCreditsResponse?)


    @Query("SELECT * From MovieImagesResponse WHERE id LIKE :id LIMIT 1")
    fun getMovieImages(id: String): MovieImagesResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieImages(images: MovieImagesResponse?)


}