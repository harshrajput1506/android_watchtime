package com.app.watchtime.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.ViewModel
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


    init {
        fetchTitles()
    }

    private fun fetchTitles() {
        repo.getTitles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { titles ->
                    _state.value = HomeState.Fetched(titles)
                    Log.d(TAG, "fetchTitles: $titles")
                },
                {error -> _state.value = HomeState.Error(error.message ?: "Something Went Wrong")}
            )
            .addTo(disposables)

    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}