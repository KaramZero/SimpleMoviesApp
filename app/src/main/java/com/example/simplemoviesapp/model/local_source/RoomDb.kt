package com.example.simplemoviesapp.model.local_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simplemoviesapp.model.data_classes.Converters
import com.example.simplemoviesapp.model.data_classes.genre_response.GenreResponse
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.MovieCreditsResponse
import com.example.simplemoviesapp.model.data_classes.movie_images_response.MovieImagesResponse
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie

@Database(
    entities = [GenreResponse::class, Movie::class, MovieCreditsResponse::class, MovieImagesResponse::class],
    version = 1
)
@TypeConverters(Converters::class)

abstract class RoomDb : RoomDatabase() {
    abstract fun genresDao(): GenresDao
    abstract fun moviesDeo(): MoviesDao
    abstract fun moviesDetailsDeo(): MoviesDetailsDao

}