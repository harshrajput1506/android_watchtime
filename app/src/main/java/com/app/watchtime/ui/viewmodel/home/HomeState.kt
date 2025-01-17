package com.app.watchtime.ui.viewmodel.home

import com.app.watchtime.data.models.Title
import com.app.watchtime.data.models.Titles

sealed class HomeState {
    data object Loading : HomeState()
    data class Fetched(
        val movieTitles : List<Title>,
        val showTitles: List<Title>,
        val isLoadingMore : Boolean = false
    ) : HomeState()
    data class Error(val message: String) : HomeState()
    data object Empty : HomeState()
}