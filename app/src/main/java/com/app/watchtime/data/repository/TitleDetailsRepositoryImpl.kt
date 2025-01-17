package com.app.watchtime.data.repository

import android.util.Log
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
        }.doOnError { error ->
            Log.e("TitleDetailsRepository", "getTvShowDetails: error fetching show details", error)
        }
    }

    override fun getMovieDetails(movieId: Int): Single<TitleDetails> {
        return apiService.getMovieDetails(movieId = movieId).map {
            it.toTitleDetails()
        }.onErrorResumeNext { error ->
            Single.error(error)
        }.doOnError { error ->
            Log.e("TitleDetailsRepository", "getMovieDetails:  error fetching movie details", error)

        }
    }
}