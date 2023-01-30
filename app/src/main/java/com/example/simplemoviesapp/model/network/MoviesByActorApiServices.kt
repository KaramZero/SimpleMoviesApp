package com.example.simplemoviesapp.model.network


import com.example.simplemoviesapp.model.data_classes.movies_by_actor_response.MoviesByActorResponse
import retrofit2.http.*

interface MoviesByActorApiServices {

    @GET("${NetworkKeys.BASE_URL}person/{actorID}/movie_credits")
    suspend fun getMoviesByActor(
        @Path("actorID") actorID: String,
        @Query("language", encoded = true) language: String,
        @HeaderMap headers: Map<String, String>
    ): MoviesByActorResponse

}