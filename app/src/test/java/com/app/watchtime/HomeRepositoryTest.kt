package com.app.watchtime

import com.app.watchtime.data.models.MovieDto
import com.app.watchtime.data.models.MovieResponse
import com.app.watchtime.data.models.TvDto
import com.app.watchtime.data.models.TvResponse
import com.app.watchtime.data.network.ApiService
import com.app.watchtime.data.repository.HomeRepository
import com.app.watchtime.data.repository.HomeRepositoryImpl
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class HomeRepositoryTest {
    @Mock
    private lateinit var apiService: ApiService
    private lateinit var homeRepository: HomeRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeRepository = HomeRepositoryImpl(apiService)
    }

    @Test
    fun `getMovies returns mapped MovieResult when API call is successful`() {
        // Given
        val page = 1
        val movieDto = MovieDto(
            adult = false,
            backdrop_path = "/backdrop.jpg",
            genre_ids = listOf(1, 2),
            id = 1,
            original_language = "en",
            original_title = "Test Movie",
            overview = "Test Overview",
            popularity = 8.5,
            poster_path = "/poster.jpg",
            release_date = "2024-01-20",
            title = "Test Movie",
            video = false,
            vote_average = 7.5,
            vote_count = 100
        )
        val movieResponse = MovieResponse(
            page = page,
            results = listOf(movieDto),
            total_pages = 10,
            total_results = 100
        )

        `when`(apiService.getMovies(page = page)).thenReturn(Single.just(movieResponse))

        // When
        val testObserver = homeRepository.getMovies(page).test()

        // Then
        testObserver.assertValue { result ->
            result.movies.size == 1 &&
                    result.page == page &&
                    result.totalPages == 10 &&
                    result.movies[0].id == movieDto.id &&
                    result.movies[0].title == movieDto.title
        }
    }

    @Test
    fun `getShows returns mapped TvShowResult when API call is successful`() {
        // Given
        val page = 1
        val tvDto = TvDto(
            adult = false,
            backdrop_path = "/backdrop.jpg",
            first_air_date = "2024-01-20",
            genre_ids = listOf(1, 2),
            id = 1,
            name = "Test Show",
            origin_country = listOf("US"),
            original_language = "en",
            original_name = "Test Show Original",
            overview = "Test Overview",
            popularity = 8.5,
            poster_path = "/poster.jpg",
            vote_average = 7.5,
            vote_count = 100
        )
        val tvResponse = TvResponse(
            page = page,
            results = listOf(tvDto),
            total_pages = 10,
            total_results = 100
        )

        `when`(apiService.getTvSeries(page = page)).thenReturn(Single.just(tvResponse))

        // When
        val testObserver = homeRepository.getShows(page).test()

        // Then
        testObserver.assertValue { result ->
            result.shows.size == 1 &&
                    result.page == page &&
                    result.totalPages == 10 &&
                    result.shows[0].id == tvDto.id &&
                    result.shows[0].title == tvDto.name
        }
    }

    @Test
    fun `getTitles returns combined results when both API calls are successful`() {
        // Given
        val movieResponse = MovieResponse(
            page = 1,
            results = listOf(
                MovieDto(
                    id = 1,
                    title = "Test Movie",
                    poster_path = "/poster.jpg",
                    vote_average = 7.5,
                    adult = false,
                    backdrop_path = "",
                    genre_ids = listOf(),
                    original_language = "en",
                    original_title = "",
                    overview = "",
                    popularity = 0.0,
                    release_date = "",
                    video = false,
                    vote_count = 0
                )
            ),
            total_pages = 10,
            total_results = 100
        )

        val tvResponse = TvResponse(
            page = 1,
            results = listOf(
                TvDto(
                    id = 2,
                    name = "Test Show",
                    poster_path = "/poster.jpg",
                    vote_average = 8.5,
                    adult = false,
                    backdrop_path = "",
                    first_air_date = "",
                    genre_ids = listOf(),
                    origin_country = listOf(),
                    original_language = "en",
                    original_name = "",
                    overview = "",
                    popularity = 0.0,
                    vote_count = 0
                )
            ),
            total_pages = 5,
            total_results = 50
        )

        `when`(apiService.getMovies(page = 1)).thenReturn(Single.just(movieResponse))
        `when`(apiService.getTvSeries(page = 1)).thenReturn(Single.just(tvResponse))

        // When
        val testObserver = homeRepository.getTitles().test()

        // Then
        testObserver.assertValue { titles ->
            titles.movieResult.movies.size == 1 &&
                    titles.tvResult.shows.size == 1 &&
                    titles.movieResult.movies[0].title == "Test Movie" &&
                    titles.tvResult.shows[0].title == "Test Show"
        }
    }

    @Test
    fun `getMovies emits error when API call fails`() {
        // Given
        val expectedError = RuntimeException("API Error")
        `when`(apiService.getMovies(page = 1)).thenReturn(Single.error(expectedError))

        // When
        val testObserver = homeRepository.getMovies(1).test()

        // Then
        testObserver
            .assertNoValues()
            .assertNotComplete()
            .assertError { error ->
                error is RuntimeException && error.message == "API Error"
            }

        verify(apiService).getMovies(page = 1)
    }
}