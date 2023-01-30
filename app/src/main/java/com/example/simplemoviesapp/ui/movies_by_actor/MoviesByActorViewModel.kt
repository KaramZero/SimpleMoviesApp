package com.example.simplemoviesapp.ui.movies_by_actor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.repository.MoviesByActorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesByActorViewModel @Inject constructor(
    private val movieDetailsRepository: MoviesByActorRepository
) : ViewModel() {


    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    private val _movies = MutableLiveData<ArrayList<Movie>>()
    val movies: LiveData<ArrayList<Movie>> = _movies

    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> = _status

    /**
     * it takes the actor id to get the movies with.
     *
     * calls the repo to get the Movies List.
     * and post the res to the MutableLiveData of ArrayList of Movies.
     * and if any Exception happens it notify the ui with error.
     */
    fun getMoviesByActor(actorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _status.postValue(Status.LOADING)

                val movies = movieDetailsRepository.getMoviesByActor(actorId)
                _movies.postValue(movies)

                _status.postValue(Status.DONE)
            } catch (ex: Exception) {
                Log.e(debugTAG, "getMoviesByActor: $ex")
                _status.postValue(Status.ERROR)
                _movies.postValue(ArrayList())
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