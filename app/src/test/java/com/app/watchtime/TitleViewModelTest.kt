package com.app.watchtime

import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.app.watchtime.data.models.TitleDetails
import com.app.watchtime.data.models.TitleType
import com.app.watchtime.data.repository.TitleDetailsRepository
import com.app.watchtime.ui.navigation.Screen
import com.app.watchtime.ui.viewmodel.title.TitleState
import com.app.watchtime.ui.viewmodel.title.TitleViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TitleViewModelTest {

    private lateinit var viewModel: TitleViewModel
    private lateinit var repository: TitleDetailsRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        repository = mockk()
        savedStateHandle = mockk()

        // Mock Android Looper
        val looper = mockk<Looper>(relaxed = true)
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns looper

        // Setup RxJava schedulers
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
        unmockkStatic(Looper::class)
    }

    @Test
    fun `fetchTitleDetails for movie success updates state to Fetched`() = runTest {
        // Given

        savedStateHandle.mockkToRoute(Screen.Title(id = 1, type = TitleType.MOVIE))

        val mockTitleDetails = TitleDetails(
            id = 1,
            title = "Movie 1",
            poster = "poster1.jpg",
            rating = 8.0,
            overview = "overview of the movie",
            releaseDate = "",
            genres = emptyList()
        )

        every { repository.getMovieDetails(1) } returns Single.just(mockTitleDetails)

        // When
        viewModel = TitleViewModel(savedStateHandle, repository)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is TitleState.Fetched)
        assertEquals(mockTitleDetails, (state as TitleState.Fetched).titleDetails)
        verify { repository.getMovieDetails(1) }
    }

    @Test
    fun `fetchTitleDetails for tv show success updates state to Fetched`() = runTest {
        // Given

        savedStateHandle.mockkToRoute(Screen.Title(id = 2, type = TitleType.TV))

        val mockTitleDetails = TitleDetails(
            id = 2,
            title = "Show 1",
            poster = "poster2.jpg",
            rating = 7.8,
            overview = "Overview of Show 1",
            releaseDate = "",
            genres = emptyList()
        )

        every { repository.getTvShowDetails(2) } returns Single.just(mockTitleDetails)

        // When
        viewModel = TitleViewModel(savedStateHandle, repository)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is TitleState.Fetched)
        assertEquals(mockTitleDetails, (state as TitleState.Fetched).titleDetails)
        verify { repository.getTvShowDetails(2) }
    }

    @Test
    fun `fetchTitleDetails for movie error updates state to Error`() = runTest {
        // Given
        savedStateHandle.mockkToRoute(Screen.Title(id = 1, type = TitleType.MOVIE))

        val errorMessage = "Network error"
        every { repository.getMovieDetails(1) } returns Single.error(Exception(errorMessage))

        // When
        viewModel = TitleViewModel(savedStateHandle, repository)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is TitleState.Error)
        assertEquals("Unknown error occurred - $errorMessage", (state as TitleState.Error).message)
        verify { repository.getMovieDetails(1) }
    }

    @Test
    fun `fetchTitleDetails for tv show error updates state to Error`() = runTest {
        // Given
        savedStateHandle.mockkToRoute(Screen.Title(id = 2, type = TitleType.TV))

        val errorMessage = "Network error"
        every { repository.getTvShowDetails(2) } returns Single.error(Exception(errorMessage))

        // When
        viewModel = TitleViewModel(savedStateHandle, repository)

        // Then
        val state = viewModel.state.first()
        assertTrue(state is TitleState.Error)
        assertEquals("Unknown error occurred - $errorMessage", (state as TitleState.Error).message)
        verify { repository.getTvShowDetails(2) }
    }

    private inline fun <reified T : Any> SavedStateHandle.mockkToRoute(screen: T) {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { toRoute<T>() } returns screen
    }

}