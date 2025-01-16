package com.app.watchtime.ui.viewmodel.home

import com.app.watchtime.data.models.Titles

sealed class HomeState {
    data object Loading : HomeState()
    data class Fetched(val titles : Titles) : HomeState()
    data class Error(val message: String) : HomeState()
}