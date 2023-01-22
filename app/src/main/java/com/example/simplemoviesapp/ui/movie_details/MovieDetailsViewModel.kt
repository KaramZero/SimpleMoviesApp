package com.example.simplemoviesapp.ui.movie_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.Posters
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.movie_credits_response.Cast
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.repository.MovieDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) : ViewModel() {


    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private val _posters = MutableLiveData<ArrayList<Posters>>()
    val posters: LiveData<ArrayList<Posters>> = _posters

    private val _cast = MutableLiveData<ArrayList<Cast>>()
    val cast: LiveData<ArrayList<Cast>> = _cast

    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> = _status

    /**
     * called to load the movies data from the API.
     * it calls the getMovieImages, getMovieDetails and getMovieCast methods.
     */
    fun loadMovieData(movieId: String) {

        getMovieImages(movieId)
        getMovieDetails(movieId)
        getMovieCast(movieId)
    }


    /**
     * it takes the movie id to get the movie with.
     *
     * calls the Movie Details repo to get the Movie Details.
     * and post the res to the MutableLiveData of Movie.
     * and if any Exception happens it notify the ui with error.
     */
    private fun getMovieDetails(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _status.postValue(Status.LOADING)

                val movie = movieDetailsRepository.getMovieDetails(movieId)
                _movie.postValue(movie)

                _status.postValue(Status.DONE)
            } catch (ex: Exception) {
                Log.e(debugTAG, "getMovieDetails: $ex")
                _status.postValue(Status.ERROR)
            }
        }


    }


    /**
     * it takes the movie id to get the movie with.
     *
     * calls the Movie Details repo to get the Movie Cast.
     * and post the res to the MutableLiveData of ArrayList of Cast.
     */
    private fun getMovieCast(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cast = movieDetailsRepository.getMovieCast(movieId)
                _cast.postValue(cast)
            } catch (ex: Exception) {
                Log.e(debugTAG, "getMovieCast: $ex")
            }
        }


    }


    /**
     * it takes the movie id to get the movie with.
     *
     * calls the Movie Details repo to get the Movie Posters.
     * and post the res to the MutableLiveData of ArrayList of Posters.
     */
    private fun getMovieImages(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posters = movieDetailsRepository.getMovieImages(movieId)
                _posters.postValue(posters)
            } catch (ex: Exception) {
                Log.e(debugTAG, "getMovieImages: $ex")
            }
        }


    }


    /**
     * is called after observing the LiveData of Status, to clear it's value.
     * so, when recreating the fragment it won't observe the old value again.
     */
    fun clearStatus() {
        if (_status.value != null) {
            _status.postValue(null)
        }
    }

}