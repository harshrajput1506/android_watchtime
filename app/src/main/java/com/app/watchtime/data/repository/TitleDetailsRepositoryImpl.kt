package com.app.watchtime.data.repository

import com.app.watchtime.data.models.TitleDetails
import com.app.watchtime.data.models.toTitleDetails
import com.app.watchtime.data.network.ApiService
import io.reactivex.rxjava3.core.Single

class TitleDetailsRepositoryImpl(
    private val apiService: ApiService
) : TitleDetailsRepository {

    override fun getTvShowDetails(tvId: Int): Single<TitleDetails> {
        return apiService.getTvShowDetails(tvId = tvId).map {
            it.toTitleDetails()
        }.onErrorResumeNext { error ->
            Single.error(error)
        }
    }

    override fun getMovieDetails(movieId: Int): Single<TitleDetails> {
        return apiService.getMovieDetails(movieId = movieId).map {
            it.toTitleDetails()
        }.onErrorResumeNext { error ->
            Single.error(error)
        }
    }
}