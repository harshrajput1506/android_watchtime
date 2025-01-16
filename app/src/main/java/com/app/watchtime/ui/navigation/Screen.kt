package com.app.watchtime.ui.navigation

import com.app.watchtime.data.models.TitleType
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home : Screen()
    @Serializable
    data class Title(val id: Int, val type: TitleType) : Screen()
}

