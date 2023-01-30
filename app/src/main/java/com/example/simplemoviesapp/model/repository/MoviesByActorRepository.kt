package com.example.simplemoviesapp.model.repository


import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.network.MoviesByActorApiServices
import com.example.simplemoviesapp.model.network.NetworkKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesByActorRepository @Inject constructor(
    private var api: MoviesByActorApiServices
    )
{

    /**
     * it takes the actor Id to get the Movies with.
     *
     * tries to get the Movies from API.
     *
     * throws Exception if couldn't fitch the Movies.
     */
    suspend fun getMoviesByActor(actorId: String): ArrayList<Movie> {

        return  api.getMoviesByActor(actorId, NetworkKeys.language, NetworkKeys.headers).movies
    }


}