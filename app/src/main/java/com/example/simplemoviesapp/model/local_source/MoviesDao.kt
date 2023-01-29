package com.example.simplemoviesapp.model.local_source

import androidx.room.*
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie?)

    @Query("SELECT * From movies WHERE genre_ids LIKE :genre LIMIT :start , :end")
    fun getMovies(genre: String, start: Int, end: Int): List<Movie>

    @Query("SELECT COUNT(*) From movies WHERE genre_ids LIKE :genre ")
    fun getMoviesCount(genre: String): Int

}