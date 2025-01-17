package com.app.watchtime

import com.app.watchtime.data.models.*
import com.app.watchtime.data.network.ApiService
import com.app.watchtime.data.repository.HomeRepository
import com.app.watchtime.data.repository.HomeRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*


class HomeRepositoryTest {

    private lateinit var repository: HomeRepository
    private val apiService: ApiService = mockk()

    @Before
    fun setUp() {
        repository = HomeRepositoryImpl(apiService)
    }

    @Test
    fun `getMovies should return movie results`() {
        // Arrange
        val mockMovieResponse = MovieResponse(
            page = 1,
            results = listOf(
                MovieDto(
                    id = 1,
                    title = "Test Movie",
                    vote_average = 8.5,
                    poster_path = "/test_movie.jpg",
                    adult = false,
                    backdrop_path = "",
                    genre_ids = emptyList(),
                    original_language = "en",
                    original_title = "Test Movie",
                    overview = "",
                    popularity = 0.0,
                    release_date = "",
                    video = false,
                    vote_count = 0
                )
            ),
            total_pages = 1,
            total_results = 1
        )

        every { apiService.getMovies(page = 1) } returns Single.just(mockMovieResponse)

        // Act
        val testObserver = repository.getMovies(1).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValue { result ->
            result.movies.size == 1 && result.movies[0].title == "Test Movie"
        }
    }

    @Test
    fun `getShows should return TV show results`() {
        // Arrange
        val mockTvResponse = TvResponse(
            page = 1,
            results = listOf(
                TvDto(
                    id = 2,
                    name = "Test Show",
                    vote_average = 9.0,
                    poster_path = "/test_show.jpg",
                    adult = false,
                    backdrop_path = "",
                    genre_ids = emptyList(),
                    original_language = "en",
                    original_name = "Test Show",
                    overview = "",
                    popularity = 0.0,
                    first_air_date = "",
                    origin_country = emptyList(),
                    vote_count = 0
                )
            ),
            total_pages = 1,
            total_results = 1
        )

        every { apiService.getTvSeries(page = 1) } returns Single.just(mockTvResponse)

        // Act
        val testObserver = repository.getShows(1).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValue { result ->
            result.shows.size == 1 && result.shows[0].title == "Test Show"
        }
    }
}
