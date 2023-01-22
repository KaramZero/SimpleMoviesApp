package com.example.simplemoviesapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemoviesapp.model.data_classes.Status
import com.example.simplemoviesapp.model.data_classes.genre_response.Genre
import com.example.simplemoviesapp.model.repository.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val genreRepository: GenreRepository,
) : ViewModel() {

    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }


    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> = _status

    private val _genres = MutableLiveData<ArrayList<Genre>>()
    val genres: LiveData<ArrayList<Genre>> = _genres

    /**
     * calls the genre repo to get the genre list.
     * and post the res to the MutableLiveData of ArrayList of Genres.
     * and if any Exception happens it notify the ui with error.
     */
    fun getGenreList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _status.postValue(Status.LOADING)
                _genres.postValue(genreRepository.getGenreList())
                _status.postValue(Status.DONE)
            } catch (ex: Exception) {
                Log.e(debugTAG, "getGenreList: $ex")
                _status.postValue(Status.ERROR)
            }
        }


    }

    fun clearStatus() {
        if (_status.value != null) {
            _status.postValue(null)
        }
    }

}