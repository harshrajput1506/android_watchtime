package com.app.watchtime.ui.viewmodel.title

import com.app.watchtime.data.models.TitleDetails

sealed class TitleState {
    data object Loading : TitleState()
    data class Fetched(val titleDetails: TitleDetails) : TitleState()
    data class Error(val message : String) : TitleState()
}