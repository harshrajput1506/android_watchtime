package com.app.watchtime.data.repository

import com.app.watchtime.data.models.Title
import com.app.watchtime.data.models.Titles
import io.reactivex.rxjava3.core.Single

interface HomeRepository {
    fun getTitles() : Single<Titles>
}