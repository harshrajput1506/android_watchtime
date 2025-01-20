package com.app.watchtime

import com.app.watchtime.data.models.MovieResult
import com.app.watchtime.data.models.Title
import com.app.watchtime.data.models.TitleType
import com.app.watchtime.data.models.Titles
import com.app.watchtime.data.models.TvShowResult
import com.app.watchtime.data.repository.HomeRepository
import com.app.watchtime.ui.viewmodel.home.HomeState
import com.app.watchtime.ui.viewmodel.home.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: HomeRepository

    @Before
    fun setup() {
        repository = mockk()

        // Provide a default response for `getTitles` to avoid this error
        every { repository.getTitles() } returns Single.just(
            Titles(
                MovieResult(emptyList(), 1, 1),
                TvShowResult(emptyList(), 1, 1)
            )
        )

        // Setup RxJava to run synchronously in tests
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `fetchTitles success updates state to Fetched`() = runTest {
        // Given
        val mockMovies = listOf(
            Title(1, "Movie 1", "poster1.jpg", 8.0),
            Title(2, "Movie 2", "poster2.jpg", 7.5)
        )
        val mockShows = listOf(
            Title(3, "Show 1", "poster3.jpg", 8.5),
            Title(4, "Show 2", "poster4.jpg", 7.8)
        )

        val mockTitles = Titles(
            MovieResult(mockMovies, 1, 10),
            TvShowResult(mockShows, 1, 10)
        )

        every { repository.getTitles() } returns Single.just(mockTitles)

        // When
        viewModel = HomeViewModel(repository)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is HomeState.Fetched)
        with(state) {
            assertEquals(mockMovies, movieTitles)
            assertEquals(mockShows, showTitles)
            assertFalse(isLoadingMore)
            assertNull(loadingMoreError)
        }
    }

    @Test
    fun `fetchTitles error updates state to Error`() = runTest {
        // Given
        val errorMessage = "Network error"
        every { repository.getTitles() } returns Single.error(Exception(errorMessage))

        // When
        viewModel = HomeViewModel(repository)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is HomeState.Error)
        assertEquals("Unknown error occurred - $errorMessage", state.message)
    }

    @Test
    fun `loadMoreTitle for movies updates state correctly`() = runTest {
        // Given
        val initialMovies = listOf(Title(1, "Movie 1", "poster1.jpg", 8.0))
        val moreMovies = listOf(Title(2, "Movie 2", "poster2.jpg", 7.5))

        val initialTitles = Titles(
            MovieResult(initialMovies, 1, 2),
            TvShowResult(emptyList(), 1, 1)
        )

        every { repository.getTitles() } returns Single.just(initialTitles)
        every { repository.getMovies(any()) } returns Single.just(MovieResult(moreMovies, 2, 2))

        // When
        viewModel = HomeViewModel(repository)
        viewModel.loadMoreTitle(TitleType.MOVIE)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is HomeState.Fetched)
        with(state) {
            assertEquals(initialMovies + moreMovies, movieTitles)
            assertFalse(isLoadingMore)
            assertNull(loadingMoreError)
        }
    }
}