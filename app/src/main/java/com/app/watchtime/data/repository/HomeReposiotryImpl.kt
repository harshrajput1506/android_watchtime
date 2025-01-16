package com.app.watchtime.data.repository

import android.util.Log
import com.app.watchtime.data.models.Title
import com.app.watchtime.data.models.Titles
import com.app.watchtime.data.models.toTitle
import com.app.watchtime.data.network.ApiService
import io.reactivex.rxjava3.core.Single

class HomeRepositoryImpl (
    private val apiService: ApiService
) : HomeRepository {

    override fun getTitles(): Single<Titles> {
       return Single.zip(
           apiService.getMovies().map { response ->
               response.results.map { it.toTitle() }
           },
           apiService.getTvSeries().map { response ->
               response.results.map { it.toTitle() }
           }
       ) { movies, series ->
           Titles(movies = movies, tvSeries = series)
       }.doOnError { error ->
           Log.e("HomeRepository", "getTitles: ", error)
           error.printStackTrace()
       }
    }
}