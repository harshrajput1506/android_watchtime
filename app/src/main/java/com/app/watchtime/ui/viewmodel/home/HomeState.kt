package com.app.watchtime.ui.viewmodel.home

import com.app.watchtime.data.models.Title

sealed class HomeState {
    data object Loading : HomeState()
    data class Fetched(
        val movieTitles : List<Title>,
        val showTitles: List<Title>,
        val isLoadingMore : Boolean = false,
        val loadingMoreError : String? = null
    ) : HomeState()
    data class Error(val message: String) : HomeState()
    data object Empty : HomeState()
}