package com.app.watchtime.data.repository

import com.app.watchtime.data.models.TitleDetails
import io.reactivex.rxjava3.core.Single

interface TitleDetailsRepository {
    fun getTvShowDetails(tvId : Int) : Single<TitleDetails>
    fun getMovieDetails(movieId : Int) : Single<TitleDetails>
}