package com.example.simplemoviesapp.model.local_source

import androidx.room.*
import com.example.simplemoviesapp.model.data_classes.genre_response.GenreResponse
import com.example.simplemoviesapp.model.data_classes.movies_list_response.MoviesListResponse

@Dao
interface MoviesDao {
    @Query("SELECT * From moviesListResponse WHERE genre LIKE :genre and page LIKE :page LIMIT 1")
    fun getMovies(genre: String,page: Int ): MoviesListResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMoviesListResponse(movies: MoviesListResponse?)
}