package com.example.simplemoviesapp.di

import android.content.Context
import androidx.room.Room
import com.example.simplemoviesapp.model.local_source.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideMoviesDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RoomDb::class.java,
        "Movies"
    ).build()

    @Singleton
    @Provides
    fun provideGenreDao(db: RoomDb) = db.genresDao()

    @Singleton
    @Provides
    fun provideMoviesDao(db: RoomDb) = db.moviesDeo()

    @Singleton
    @Provides
    fun provideMoviesDetailsDeo(db: RoomDb) = db.moviesDetailsDeo()
}