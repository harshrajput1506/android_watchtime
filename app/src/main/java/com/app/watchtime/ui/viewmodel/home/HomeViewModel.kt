package com.app.watchtime.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.app.watchtime.core.error_handler.ApiErrorHandler
import com.app.watchtime.data.models.TitleType
import com.app.watchtime.data.repository.HomeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel (
    private val repo: HomeRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state get() = _state

    // CompositeDisposable to manage disposables
    private val disposables = CompositeDisposable()

    companion object {
        const val TAG = "HomeViewModel"
    }

    private var moviesCurrentPage : Int = 1
    private var showsCurrentPage : Int = 1
    private var moviesTotalPages : Int = 200 // Default Total Pages
    private var showsTotalPages : Int = 200 // Default Total Pages

    private var hasMorePages = true
    private var isLoadingMore = false


    init {
        fetchTitles()
    }

    private fun fetchTitles() {
        repo.getTitles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { titles ->
                    _state.value = when {
                        titles.tvResult.shows.isEmpty() && titles.movieResult.movies.isEmpty() -> {
                            HomeState.Error("Something went wrong")
                        }
                        else -> {
                            HomeState.Fetched(
                                movieTitles = titles.movieResult.movies,
                                showTitles = titles.tvResult.shows
                            )
                        }
                    }
                    Log.d(TAG, "fetchTitles: $titles")
                    moviesTotalPages = titles.movieResult.totalPages
                    showsTotalPages = titles.tvResult.totalPages
                    moviesCurrentPage++
                    showsCurrentPage++
                },
                {error ->
                    val message = ApiErrorHandler().handleError(error)
                    _state.value = HomeState.Error(message) }
            )
            .addTo(disposables)

    }

    fun loadMoreTitle(type: TitleType) {
        if(isLoadingMore || !hasMorePages) return

        isLoadingMore = true
        _state.value = when {
            _state.value is HomeState.Fetched -> {
                (_state.value as HomeState.Fetched).copy(
                    isLoadingMore = true
                )
            }
            else -> _state.value
        }

        when(type){
            TitleType.MOVIE -> {
                if(moviesCurrentPage > moviesTotalPages) return
                loadMoreMovies()
            }
            TitleType.TV -> {
                if(showsCurrentPage > showsTotalPages) return
                loadMoreSeries()
            }
        }
    }

    private fun loadMoreMovies() {
        repo.getMovies(moviesCurrentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { movieResult ->
                    val currentState = _state.value

                    _state.value = when {
                        movieResult.movies.isEmpty() -> {
                            hasMorePages = false
                            if(currentState is HomeState.Fetched) {
                                currentState
                            } else {
                                HomeState.Empty
                            }
                        }
                        currentState is HomeState.Fetched -> {
                            currentState.copy(
                                movieTitles = currentState.movieTitles + movieResult.movies,
                                isLoadingMore = false,
                                loadingMoreError = null
                            )
                        }
                         else -> currentState
                    }
                    moviesCurrentPage++
                    isLoadingMore = false
                },
                { error ->
                    val message = ApiErrorHandler().handleError(error)
                    _state.value = when {
                        _state.value is HomeState.Fetched -> {
                            (_state.value as HomeState.Fetched).copy(
                                loadingMoreError = message,
                                isLoadingMore = false
                            )
                        }
                        else -> HomeState.Error(message)
                    }
                    isLoadingMore = false
                }
            ).addTo(disposables)
    }

    private fun loadMoreSeries() {
        repo.getShows(showsCurrentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { showResult ->
                    val currentState = _state.value

                    _state.value = when {
                        showResult.shows.isEmpty() -> {
                            hasMorePages = false
                            if(currentState is HomeState.Fetched) {
                                currentState
                            } else {
                                HomeState.Empty
                            }
                        }
                        currentState is HomeState.Fetched -> {
                            currentState.copy(
                                showTitles = currentState.showTitles + showResult.shows,
                                isLoadingMore = false,
                                loadingMoreError = null
                            )
                        }
                        else -> currentState
                    }
                    showsCurrentPage++
                    isLoadingMore = false
                },
                { error ->
                    val message = ApiErrorHandler().handleError(error)
                    _state.value = when {
                        _state.value is HomeState.Fetched -> {
                            (_state.value as HomeState.Fetched).copy(
                                loadingMoreError = message,
                                isLoadingMore = false
                            )
                        }
                        else -> HomeState.Error(message)
                    }
                    isLoadingMore = false
                }
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}