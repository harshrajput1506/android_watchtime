package com.app.watchtime.data.network

import com.app.watchtime.data.models.MovieDetailsDto
import com.app.watchtime.data.models.MovieResponse
import com.app.watchtime.data.models.TvResponse
import com.app.watchtime.data.models.TvShowDetailsDto
import com.app.watchtime.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    fun getMovies(
        @Header("Authorization") token: String = "Bearer ${Constants.ACCESS_TOKEN}",
        @Header("accept") accept: String = "application/json",
        @Query("include_adult") isAdult: Boolean = true,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = DEFAULT_SORT,
    ): Single<MovieResponse>

    @GET("discover/tv")
    fun getTvSeries(
        @Header("Authorization") token: String = "Bearer ${Constants.ACCESS_TOKEN}",
        @Header("accept") accept: String = "application/json",
        @Query("include_adult") isAdult: Boolean = true,
        @Query("include_null_first_air_dates") includeFirstAirDates: Boolean = false,
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = DEFAULT_SORT,
    ): Single<TvResponse>

    @GET("tv/{id}")
    fun getTvShowDetails(
        @Header("Authorization") token: String = "Bearer ${Constants.ACCESS_TOKEN}",
        @Header("accept") accept: String = "application/json",
        @Path("id") tvId: Int,
        @Query("language") language: String = DEFAULT_LANGUAGE,
    ): Single<TvShowDetailsDto>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Header("Authorization") token: String = "Bearer ${Constants.ACCESS_TOKEN}",
        @Header("accept") accept: String = "application/json",
        @Path("id") movieId: Int,
        @Query("language") language: String = DEFAULT_LANGUAGE,
    ): Single<MovieDetailsDto>

    companion object {
        const val INITIAL_PAGE = 1
        const val DEFAULT_LANGUAGE = "en-US"
        const val DEFAULT_SORT = "popularity.desc"
    }
}

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}