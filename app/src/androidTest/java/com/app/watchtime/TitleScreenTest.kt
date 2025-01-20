package com.app.watchtime

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.app.watchtime.data.models.Genre
import com.app.watchtime.data.models.TitleDetails
import com.app.watchtime.ui.screens.TitleScreen
import com.app.watchtime.ui.viewmodel.title.TitleState
import com.app.watchtime.ui.viewmodel.title.TitleViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class TitleScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<TitleViewModel>()
    private val mockNavigateBack: () -> Unit = mockk(relaxed = true)

    @Test
    fun whenStateIsLoading_shouldShowShimmerEffect() {
        // Given
        val state = MutableStateFlow<TitleState>(TitleState.Loading)
        every { mockViewModel.state } returns state

        // When
        composeTestRule.setContent {
            TitleScreen(
                viewModel = mockViewModel,
                onNavigateBack = mockNavigateBack
            )
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Shimmer Effect").assertExists()
    }

    @Test
    fun whenStateIsFetched_shouldShowTitleDetails() {
        // Given
        val titleDetails = TitleDetails(
            id = 1,
            title = "Test Movie",
            overview = "Test Overview",
            poster = "poster_url",
            releaseDate = "2024",
            rating = 7.5,
            genres = listOf(Genre(1, "Action"), Genre(2, "Drama"))
        )
        val state = MutableStateFlow<TitleState>(TitleState.Fetched(titleDetails))
        every { mockViewModel.state } returns state

        // When
        composeTestRule.setContent {
            TitleScreen(
                viewModel = mockViewModel,
                onNavigateBack = mockNavigateBack
            )
        }

        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Test Movie").assertExists()
        composeTestRule.onNodeWithText("Test Overview").assertExists()
        composeTestRule.onNodeWithText("2024").assertExists()
        composeTestRule.onNodeWithText("Action").assertExists()
        composeTestRule.onNodeWithText("Drama").assertExists()

        // Verify user score is displayed
        composeTestRule.onNodeWithText("75").assertExists()
    }

    @Test
    fun whenStateIsError_shouldShowErrorMessage() {
        // Given
        val errorMessage = "An error occurred"
        val state = MutableStateFlow<TitleState>(TitleState.Error(errorMessage))
        every { mockViewModel.state } returns state

        // When
        composeTestRule.setContent {
            TitleScreen(
                viewModel = mockViewModel,
                onNavigateBack = mockNavigateBack
            )
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun whenBackButtonClicked_shouldNavigateBack() {
        // Given
        val state = MutableStateFlow<TitleState>(TitleState.Loading)
        every { mockViewModel.state } returns state

        // When
        composeTestRule.setContent {
            TitleScreen(
                viewModel = mockViewModel,
                onNavigateBack = mockNavigateBack
            )
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Back Icon").performClick()
        composeTestRule.waitForIdle()
        verify { mockNavigateBack.invoke() }
    }
}