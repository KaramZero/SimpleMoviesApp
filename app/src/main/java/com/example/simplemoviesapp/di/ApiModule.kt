package com.example.simplemoviesapp.di

import com.example.simplemoviesapp.model.network.DiscoverMoviesApiServices
import com.example.simplemoviesapp.model.network.GenreListApiServices
import com.example.simplemoviesapp.model.network.MovieDetailsApiServices
import com.example.simplemoviesapp.model.network.SearchMoviesApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideGenreListApiServices(retrofit: Retrofit): GenreListApiServices =
        retrofit.create(GenreListApiServices::class.java)

    @Singleton
    @Provides
    fun provideDiscoverMoviesApiServices(retrofit: Retrofit): DiscoverMoviesApiServices =
        retrofit.create(DiscoverMoviesApiServices::class.java)


    @Singleton
    @Provides
    fun provideMovieDetailsApiServices(retrofit: Retrofit): MovieDetailsApiServices =
        retrofit.create(MovieDetailsApiServices::class.java)


    @Singleton
    @Provides
    fun provideSearchMoviesApiServices(retrofit: Retrofit): SearchMoviesApiServices =
        retrofit.create(SearchMoviesApiServices::class.java)
}