package com.app.watchtime.data.models

import com.app.watchtime.utils.Constants


data class Title (
    val id: Int,
    val title: String,
    val poster: String,
    val rating: Double
)

data class Titles (
    val movies : List<Title>,
    val tvSeries : List<Title>
)

data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)

data class MovieDto(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class TvResponse(
    val page: Int,
    val results: List<TvDto>,
    val total_pages: Int,
    val total_results: Int
)


data class TvDto(
    val adult: Boolean,
    val backdrop_path: String,
    val first_air_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int
)

fun MovieDto.toTitle() : Title {
    return Title(
        id = id,
        title = title,
        rating = vote_average,
        poster = "${Constants.IMAGE_BASE_URL}$poster_path"
    )
}

fun TvDto.toTitle() : Title {
    return Title(
        id = id,
        title = name,
        rating = vote_average,
        poster = "${Constants.IMAGE_BASE_URL}$poster_path"
    )
}

enum class TitleType {
    MOVIE, TV
}