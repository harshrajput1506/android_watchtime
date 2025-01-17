package com.app.watchtime

import com.app.watchtime.data.models.Genre
import com.app.watchtime.data.models.LastEpisodeToAir
import com.app.watchtime.data.models.MovieDetailsDto
import com.app.watchtime.data.models.TvShowDetailsDto
import com.app.watchtime.data.models.toTitleDetails
import com.app.watchtime.data.network.ApiService
import com.app.watchtime.data.repository.TitleDetailsRepository
import com.app.watchtime.data.repository.TitleDetailsRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class TitleDetailsRepositoryTest {

    private lateinit var repository: TitleDetailsRepository
    private val apiService: ApiService = mockk()

    @Before
    fun setUp() {
        repository = TitleDetailsRepositoryImpl(apiService)
    }

    @Test
    fun `getMovieDetails should return movie details`() {
        // Arrange
        val mockMovieDetailsDto = MovieDetailsDto(
            id = 1,
            title = "Test Movie",
            poster_path = "/test_movie.jpg",
            vote_average = 8.5,
            overview = "Test movie overview",
            release_date = "2022-01-01",
            genres = listOf(
                Genre(id = 1, name = "Action"),
                Genre(id = 2, name = "Adventure")
            ),
            adult = false,
            backdrop_path = "",
            belongs_to_collection = Any(),
            budget = 1000000,
            homepage = "",
            imdb_id = "",
            origin_country = emptyList(),
            original_language = "en",
            original_title = "Test Movie",
            popularity = 0.0,
            production_companies = emptyList(),
            production_countries = emptyList(),
            revenue = 0,
            runtime = 120,
            spoken_languages = emptyList(),
            status = "",
            tagline = "",
            video = false,
            vote_count = 0
        )

        val expectedTitleDetails = mockMovieDetailsDto.toTitleDetails()

        every { apiService.getMovieDetails(movieId = 1) } returns Single.just(mockMovieDetailsDto)

        // Act
        val testObserver = repository.getMovieDetails(1).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValue { result ->
            result == expectedTitleDetails
        }
    }

    @Test
    fun `getTvShowDetails should return TV show details`() {
        // Arrange
        val mockTvShowDetailsDto = TvShowDetailsDto(
            id = 2,
            name = "Test Show",
            poster_path = "/test_show.jpg",
            vote_average = 9.0,
            overview = "Test show overview",
            first_air_date = "2022-01-01",
            genres = listOf(
                Genre(id = 3, name = "Drama"),
                Genre(id = 4, name = "Mystery")
            ),
            adult = false,
            backdrop_path = "",
            created_by = emptyList(),
            episode_run_time = emptyList(),
            homepage = "",
            in_production = true,
            languages = emptyList(),
            last_air_date = "",
            last_episode_to_air = LastEpisodeToAir(
                air_date = "",
                episode_number = 0,
                episode_type = "",
                id = 0,
                name = "",
                overview = "",
                production_code = "",
                runtime = 0,
                season_number = 0,
                show_id = 0,
                still_path = "",
                vote_average = 0.0,
                vote_count = 0
            ),
            networks = emptyList(),
            next_episode_to_air = null,
            number_of_episodes = 10,
            number_of_seasons = 1,
            origin_country = emptyList(),
            original_language = "en",
            original_name = "Test Show",
            popularity = 0.0,
            production_companies = emptyList(),
            production_countries = emptyList(),
            seasons = emptyList(),
            spoken_languages = emptyList(),
            status = "",
            tagline = "",
            type = "",
            vote_count = 0
        )

        val expectedTitleDetails = mockTvShowDetailsDto.toTitleDetails()

        every { apiService.getTvShowDetails(tvId = 2) } returns Single.just(mockTvShowDetailsDto)

        // Act
        val testObserver = repository.getTvShowDetails(2).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValue { result ->
            result == expectedTitleDetails
        }
    }

    @Test
    fun `getMovieDetails should propagate API errors`() {
        // Arrange
        val errorMessage = "Network error"
        every { apiService.getMovieDetails(movieId = 1) } returns Single.error(Throwable(errorMessage))

        // Act
        val testObserver = repository.getMovieDetails(1).test()

        // Assert
        testObserver.assertError { throwable ->
            throwable.message == errorMessage
        }
    }

    @Test
    fun `getTvShowDetails should propagate API errors`() {
        // Arrange
        val errorMessage = "Server error"
        every { apiService.getTvShowDetails(tvId = 2) } returns Single.error(Throwable(errorMessage))

        // Act
        val testObserver = repository.getTvShowDetails(2).test()

        // Assert
        testObserver.assertError { throwable ->
            throwable.message == errorMessage
        }
    }

    @Test
    fun `getMovieDetails should handle empty genres and overview`() {
        // Arrange
        val mockMovieDetailsDto = MovieDetailsDto(
            id = 1,
            title = "Test Movie",
            poster_path = "/test_movie.jpg",
            vote_average = 8.5,
            overview = "", // Empty overview
            release_date = "2022-01-01",
            genres = emptyList(), // Empty genres
            adult = false,
            backdrop_path = "",
            belongs_to_collection = Any(),
            budget = 0,
            homepage = "",
            imdb_id = "",
            origin_country = emptyList(),
            original_language = "en",
            original_title = "Test Movie",
            popularity = 0.0,
            production_companies = emptyList(),
            production_countries = emptyList(),
            revenue = 0,
            runtime = 0,
            spoken_languages = emptyList(),
            status = "",
            tagline = "",
            video = false,
            vote_count = 0
        )

        every { apiService.getMovieDetails(movieId = 1) } returns Single.just(mockMovieDetailsDto)

        // Act
        val testObserver = repository.getMovieDetails(1).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValue { result ->
            result.overview.isEmpty() && result.genres.isEmpty()
        }
    }

    @Test
    fun `getTvShowDetails should handle empty genres and overview`() {
        // Arrange
        val mockTvShowDetailsDto = TvShowDetailsDto(
            id = 2,
            name = "Test Show",
            poster_path = "/test_show.jpg",
            vote_average = 9.0,
            overview = "", // Empty overview
            first_air_date = "2022-01-01",
            genres = emptyList(), // Empty genres
            adult = false,
            backdrop_path = "",
            created_by = emptyList(),
            episode_run_time = emptyList(),
            homepage = "",
            in_production = true,
            languages = emptyList(),
            last_air_date = "",
            last_episode_to_air = null,
            networks = emptyList(),
            next_episode_to_air = null,
            number_of_episodes = 10,
            number_of_seasons = 1,
            origin_country = emptyList(),
            original_language = "en",
            original_name = "Test Show",
            popularity = 0.0,
            production_companies = emptyList(),
            production_countries = emptyList(),
            seasons = emptyList(),
            spoken_languages = emptyList(),
            status = "",
            tagline = "",
            type = "",
            vote_count = 0
        )

        every { apiService.getTvShowDetails(tvId = 2) } returns Single.just(mockTvShowDetailsDto)

        // Act
        val testObserver = repository.getTvShowDetails(2).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValue { result ->
            result.overview.isEmpty() && result.genres.isEmpty()
        }
    }

    @Test
    fun `getMovieDetails should return error for invalid movie ID`() {
        // Arrange
        every { apiService.getMovieDetails(movieId = -1) } returns Single.error(Throwable("Invalid ID"))

        // Act
        val testObserver = repository.getMovieDetails(-1).test()

        // Assert
        testObserver.assertError { throwable ->
            throwable.message == "Invalid ID"
        }
    }

    @Test
    fun `getTvShowDetails should return error for invalid TV ID`() {
        // Arrange
        every { apiService.getTvShowDetails(tvId = -1) } returns Single.error(Throwable("Invalid ID"))

        // Act
        val testObserver = repository.getTvShowDetails(-1).test()

        // Assert
        testObserver.assertError { throwable ->
            throwable.message == "Invalid ID"
        }
    }

    @Test
    fun `getMovieDetails should handle partial data`() {
        // Arrange
        val mockMovieDetailsDto = MovieDetailsDto(
            id = 1,
            title = "", // Missing title
            poster_path = "", // Missing poster path
            vote_average = 0.0, // Default rating
            overview = "Partial data overview",
            release_date = "2022-01-01",
            genres = emptyList(), // Missing genres
            adult = false,
            backdrop_path = "",
            belongs_to_collection = Any(),
            budget = 0,
            homepage = "",
            imdb_id = "",
            origin_country = emptyList(),
            original_language = "en",
            original_title = "",
            popularity = 0.0,
            production_companies = emptyList(),
            production_countries = emptyList(),
            revenue = 0,
            runtime = 0,
            spoken_languages = emptyList(),
            status = "",
            tagline = "",
            video = false,
            vote_count = 0
        )

        every { apiService.getMovieDetails(movieId = 1) } returns Single.just(mockMovieDetailsDto)

        // Act
        val testObserver = repository.getMovieDetails(1).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValue { result ->
            result.title.isEmpty() && result.poster.isEmpty() && result.rating == 0.0
        }
    }
}