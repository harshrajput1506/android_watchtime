package com.app.watchtime.data.models

import com.app.watchtime.utils.Constants

data class TitleDetails (
    val id: Int,
    val title: String,
    val poster: String,
    val rating: Double,
    val overview : String,
    val releaseDate : String,
    val genres: List<Genre>
)

fun MovieDetailsDto.toTitleDetails() : TitleDetails {
    return TitleDetails(
        id = id,
        title = title,
        poster = "${Constants.IMAGE_BASE_URL}$poster_path",
        rating = vote_average,
        overview = overview,
        releaseDate = release_date,
        genres = genres
    )
}

fun TvShowDetailsDto.toTitleDetails() : TitleDetails {
    return TitleDetails(
        id = id,
        title = name,
        poster = "${Constants.IMAGE_BASE_URL}$poster_path",
        rating = vote_average,
        overview = overview,
        releaseDate = first_air_date,
        genres = genres
    )
}

data class MovieDetailsDto(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class TvShowDetailsDto(
    val adult: Boolean,
    val backdrop_path: String,
    val created_by: List<CreatedBy>,
    val episode_run_time: List<Any>,
    val first_air_date: String,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val last_episode_to_air: LastEpisodeToAir?,
    val name: String,
    val networks: List<Network>,
    val next_episode_to_air: NextEpisodeToAir?,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val seasons: List<Season>,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
)

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
)

data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)

data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)



data class CreatedBy(
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val original_name: String,
    val profile_path: String
)

data class NextEpisodeToAir(
    val air_date: String,
    val episode_number: Int,
    val episode_type: String,
    val id: Int,
    val name: String,
    val overview: String,
    val production_code: String,
    val runtime: Any,
    val season_number: Int,
    val show_id: Int,
    val still_path: Any,
    val vote_average: Int,
    val vote_count: Int
)

data class Network(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
)

data class Season(
    val air_date: String,
    val episode_count: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    val season_number: Int,
    val vote_average: Double
)

data class LastEpisodeToAir(
    val air_date: String,
    val episode_number: Int,
    val episode_type: String,
    val id: Int,
    val name: String,
    val overview: String,
    val production_code: String,
    val runtime: Int,
    val season_number: Int,
    val show_id: Int,
    val still_path: String,
    val vote_average: Double,
    val vote_count: Int
)