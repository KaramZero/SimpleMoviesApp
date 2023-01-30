package com.example.simplemoviesapp.ui.movies_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }


    private var totalPages = 0L
    private var nextPage = 1

    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> = _status

    private val _movies = MutableLiveData<ArrayList<Movie>>()
    val movies: LiveData<ArrayList<Movie>> = _movies

    fun getMovies(genreId: Long) {
        totalPages = 0
        nextPage = 1
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _status.postValue(Status.LOADING)

                val res =
                    if (genreId == 0L)
                        moviesRepository.getMovies(page = nextPage)
                    else
                        moviesRepository.getMovies(genreId.toString(), nextPage)

                totalPages = res.totalPages ?: 0
                nextPage = res.page
                _movies.postValue(res.results)
                _status.postValue(Status.DONE)
            } catch (ex: Exception) {
                Log.e(debugTAG, "getMovies: $ex")
                _movies.postValue(ArrayList())
                _status.postValue(Status.ERROR)
            }
        }


    }


    fun getNextMovies(genreId: Long) {
        if (nextPage < totalPages) {
            nextPage++

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val res =
                        if (genreId == 0L)
                            moviesRepository.getMovies(page = nextPage)
                        else
                            moviesRepository.getMovies(genreId.toString(), nextPage)

                    totalPages = res.totalPages ?: 0

                    // appending res to the old list
                    val newList = _movies.value ?: ArrayList()
                    newList.addAll(res.results)

                    _movies.postValue(newList)

                } catch (ex: Exception) {
                    Log.e(debugTAG, "getNextMovies: $ex")
                }
            }
        }

    }

    fun clearStatus() {
        if (_status.value != null) {
            _status.postValue(null)
        }
    }

}