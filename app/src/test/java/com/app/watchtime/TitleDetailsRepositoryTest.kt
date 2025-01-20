package com.app.watchtime

import com.app.watchtime.data.models.Genre
import com.app.watchtime.data.models.MovieDetailsDto
import com.app.watchtime.data.models.TvShowDetailsDto
import com.app.watchtime.data.network.ApiService
import com.app.watchtime.data.repository.TitleDetailsRepository
import com.app.watchtime.data.repository.TitleDetailsRepositoryImpl
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TitleDetailsRepositoryTest {
    @Mock
    private lateinit var apiService: ApiService
    private lateinit var titleDetailsRepository: TitleDetailsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        titleDetailsRepository = TitleDetailsRepositoryImpl(apiService)
    }

    @Test
    fun `getMovieDetails returns mapped TitleDetails when API call is successful`() {
        // Given
        val movieId = 1
        val movieDetailsDto = MovieDetailsDto(
            id = movieId,
            title = "Test Movie",
            poster_path = "/poster.jpg",
            vote_average = 7.5,
            overview = "Test Overview",
            release_date = "2024-01-20",
            genres = listOf(Genre(1, "Action")),
            adult = false,
            backdrop_path = "",
            belongs_to_collection = null,
            budget = 0,
            homepage = "",
            imdb_id = "",
            origin_country = listOf(),
            original_language = "en",
            original_title = "",
            popularity = 0.0,
            production_companies = listOf(),
            production_countries = listOf(),
            revenue = 0,
            runtime = 0,
            spoken_languages = listOf(),
            status = "",
            tagline = "",
            video = false,
            vote_count = 0
        )

        `when`(apiService.getMovieDetails(movieId = movieId)).thenReturn(Single.just(movieDetailsDto))

        // When
        val testObserver = titleDetailsRepository.getMovieDetails(movieId).test()

        // Then
        testObserver.assertValue { result ->
            result.id == movieId &&
                    result.title == "Test Movie" &&
                    result.overview == "Test Overview" &&
                    result.releaseDate == "2024-01-20" &&
                    result.genres.size == 1 &&
                    result.genres[0].name == "Action"
        }
    }

    @Test
    fun `getTvShowDetails returns mapped TitleDetails when API call is successful`() {
        // Given
        val tvId = 1
        val tvShowDetailsDto = TvShowDetailsDto(
            id = tvId,
            name = "Test Show",
            poster_path = "/poster.jpg",
            vote_average = 8.5,
            overview = "Test Overview",
            first_air_date = "2024-01-20",
            genres = listOf(Genre(1, "Drama")),
            adult = false,
            backdrop_path = "",
            created_by = listOf(),
            episode_run_time = listOf(),
            homepage = "",
            in_production = true,
            languages = listOf(),
            last_air_date = "",
            last_episode_to_air = null,
            networks = listOf(),
            next_episode_to_air = null,
            number_of_episodes = 10,
            number_of_seasons = 1,
            origin_country = listOf(),
            original_language = "en",
            original_name = "",
            popularity = 0.0,
            production_companies = listOf(),
            production_countries = listOf(),
            seasons = listOf(),
            spoken_languages = listOf(),
            status = "",
            tagline = "",
            type = "",
            vote_count = 0
        )

        `when`(apiService.getTvShowDetails(tvId = tvId)).thenReturn(Single.just(tvShowDetailsDto))

        // When
        val testObserver = titleDetailsRepository.getTvShowDetails(tvId).test()

        // Then
        testObserver.assertValue { result ->
            result.id == tvId &&
                    result.title == "Test Show" &&
                    result.overview == "Test Overview" &&
                    result.releaseDate == "2024-01-20" &&
                    result.genres.size == 1 &&
                    result.genres[0].name == "Drama"
        }
    }

    @Test
    fun `getMovieDetails emits error when API call fails`() {
        // Given
        val movieId = 1
        val error = RuntimeException("API Error")
        `when`(apiService.getMovieDetails(movieId = movieId)).thenReturn(Single.error(error))

        // When
        val testObserver = titleDetailsRepository.getMovieDetails(movieId).test()

        // Then
        testObserver.assertError(error)
    }

    @Test
    fun `getTvShowDetails emits error when API call fails`() {
        // Given
        val tvId = 1
        val error = RuntimeException("API Error")
        `when`(apiService.getTvShowDetails(tvId = tvId)).thenReturn(Single.error(error))

        // When
        val testObserver = titleDetailsRepository.getTvShowDetails(tvId).test()

        testObserver.assertError(error)
    }
}