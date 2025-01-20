package com.app.watchtime.data.repository

import android.util.Log
import com.app.watchtime.data.models.MovieResult
import com.app.watchtime.data.models.Titles
import com.app.watchtime.data.models.TvShowResult
import com.app.watchtime.data.models.toTitle
import com.app.watchtime.data.network.ApiService
import io.reactivex.rxjava3.core.Single

class HomeRepositoryImpl(
    private val apiService: ApiService
) : HomeRepository {

    companion object {
        private const val TAG = "HomeRepository"
    }

    override fun getTitles(): Single<Titles> {
        return Single.zip(
            getMovies(ApiService.INITIAL_PAGE).onErrorResumeNext { error ->
                Log.e(TAG, "Error fetching movies", error)
                Single.just(MovieResult(emptyList(), 1, 1))
            },
            getShows(ApiService.INITIAL_PAGE).onErrorResumeNext { error ->
                Log.e(TAG, "Error fetching TV shows", error)
                Single.just(TvShowResult(emptyList(), 1, 1))
            }
        ) { movieResult: MovieResult, tvResult: TvShowResult ->
            Titles(movieResult = movieResult, tvResult = tvResult)
        }.onErrorResumeNext { error ->
            Single.error(error)
        }
    }

    override fun getMovies(page: Int): Single<MovieResult> {
        return apiService.getMovies(page = page)
            .map { response ->
                MovieResult(
                    movies = response.results.map { it.toTitle() },
                    page = response.page,
                    totalPages = response.total_pages
                )
            }
            .onErrorResumeNext { error ->
                Single.error(error)
            }
    }

    override fun getShows(page: Int): Single<TvShowResult> {
        return apiService.getTvSeries(page = page)
            .map { response ->
                TvShowResult(
                    shows = response.results.map { it.toTitle() },
                    page = response.page,
                    totalPages = response.total_pages
                )
            }
            .onErrorResumeNext { error ->
                Single.error(error)
            }
    }

}
