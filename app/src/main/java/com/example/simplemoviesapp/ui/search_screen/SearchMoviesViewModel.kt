package com.example.simplemoviesapp.ui.search_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.movies_list_response.Movie
import com.example.simplemoviesapp.model.repository.SearchMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMoviesViewModel @Inject constructor(
    private val repository: SearchMoviesRepository
) : ViewModel() {

    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }


    private var totalPages = 0L
    private var nextPage = 1

    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> = _status

    private val _movies = MutableLiveData<ArrayList<Movie>>()
    val movies: LiveData<ArrayList<Movie>> = _movies

    fun searchForMovies(query: String) {
        totalPages = 0
        nextPage = 1
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _status.postValue(Status.LOADING)

                val res =  repository.searchForMovies(query, nextPage)

                totalPages = res.totalPages ?: 0
                nextPage = res.page
                _movies.postValue(res.results)

                _status.postValue(Status.DONE)
            } catch (ex: Exception) {
                Log.e(debugTAG, "searchForMovies: $ex")
                _status.postValue(Status.ERROR)
            }

        }
    }


    fun getNextMovies(query: String) {
        if (nextPage < totalPages)
            viewModelScope.launch(Dispatchers.IO) {

                try {
                    nextPage++

                    val res =  repository.searchForMovies(query, nextPage)

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

    fun clearStatus(){
        if (_status.value!= null){
            _status.postValue(null)
        }
    }

}