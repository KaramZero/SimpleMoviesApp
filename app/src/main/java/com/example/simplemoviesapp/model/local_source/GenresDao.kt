package com.example.simplemoviesapp.model.local_source

import androidx.room.*
import com.example.simplemoviesapp.model.data_classes.genre_response.GenreResponse

@Dao
interface GenresDao {
    @Query("SELECT * From genreResponse")
    fun getGenreResponse(): GenreResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenreResponse(genres: GenreResponse?)
}