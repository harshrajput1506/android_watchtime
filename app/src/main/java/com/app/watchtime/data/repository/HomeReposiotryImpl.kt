package com.app.watchtime.data.repository

import android.util.Log
import com.app.watchtime.data.models.MovieResult
import com.app.watchtime.data.models.Title
import com.app.watchtime.data.models.Titles
import com.app.watchtime.data.models.TvShowResult
import com.app.watchtime.data.models.toTitle
import com.app.watchtime.data.network.ApiService
import io.reactivex.rxjava3.core.Single

class HomeRepositoryImpl (
    private val apiService: ApiService
) : HomeRepository {

    override fun getTitles(): Single<Titles> {
       return Single.zip(
           apiService.getMovies().map { response ->
               MovieResult (
                   movies = response.results.map { it.toTitle() },
                   page = response.page,
                   totalPages = response.total_pages
               )
           },
           apiService.getTvSeries().map { response ->
               TvShowResult (
                   shows = response.results.map { it.toTitle() },
                   page = response.page,
                   totalPages = response.total_pages
               )

           }
       ) { movieResult : MovieResult, tvResult : TvShowResult ->
           Titles(movieResult = movieResult, tvResult = tvResult)
       }.doOnError { error ->
           Log.e("HomeRepository", "getTitles: ", error)
           error.printStackTrace()
           throw error
       }
    }

    override fun getMovies(page: Int): Single<MovieResult> {
        return apiService.getMovies(page = page).map { response ->
            MovieResult (
                movies = response.results.map { it.toTitle() },
                page = response.page,
                totalPages = response.total_pages
            )
        }.doOnError { error ->
            Log.e("HomeRepository", "getTitles: ", error)
            error.printStackTrace()
            throw error
        }
    }

    override fun getShows(page: Int): Single<TvShowResult> {
        return apiService.getTvSeries(page = page).map { response ->
            TvShowResult (
                shows = response.results.map { it.toTitle() },
                page = response.page,
                totalPages = response.total_pages
            )
        }.doOnError { error ->
            Log.e("HomeRepository", "getTitles: ", error)
            error.printStackTrace()
            throw error
        }
    }
}