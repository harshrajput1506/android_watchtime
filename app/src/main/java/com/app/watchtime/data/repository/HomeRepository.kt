package com.app.watchtime.data.repository

import com.app.watchtime.data.models.MovieResult
import com.app.watchtime.data.models.Titles
import com.app.watchtime.data.models.TvShowResult
import io.reactivex.rxjava3.core.Single

interface HomeRepository {
    fun getTitles() : Single<Titles>
    fun getMovies(page: Int) : Single<MovieResult>
    fun getShows(page: Int) : Single<TvShowResult>
}