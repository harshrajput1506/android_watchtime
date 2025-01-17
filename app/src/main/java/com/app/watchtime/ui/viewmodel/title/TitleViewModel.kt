package com.app.watchtime.ui.viewmodel.title

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.app.watchtime.core.error_handler.ApiErrorHandler
import com.app.watchtime.data.models.TitleType
import com.app.watchtime.data.repository.TitleDetailsRepository
import com.app.watchtime.ui.navigation.Screen
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow

class TitleViewModel (
    savedStateHandle: SavedStateHandle,
    private val repo : TitleDetailsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val title = savedStateHandle.toRoute<Screen.Title>()
    private val _state = MutableStateFlow<TitleState>(TitleState.Loading)
    val state get() = _state

    init {
        fetchTitleDetails()
    }

    private fun fetchTitleDetails() {
        when(title.type) {
            TitleType.MOVIE -> {
                repo.getMovieDetails(title.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {titleDetails -> _state.value = TitleState.Fetched(titleDetails)},
                        { error ->
                            val message = ApiErrorHandler().handleError(error)
                            _state.value = TitleState.Error(message) }
                    ).addTo(disposables)
            }
            TitleType.TV -> {
                repo.getTvShowDetails(title.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {titleDetails -> _state.value = TitleState.Fetched(titleDetails)},
                        { error ->
                            val message = ApiErrorHandler().handleError(error)
                            _state.value = TitleState.Error(message)
                        }
                    ).addTo(disposables)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}