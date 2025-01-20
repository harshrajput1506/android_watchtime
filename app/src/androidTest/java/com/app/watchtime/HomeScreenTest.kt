package com.app.watchtime

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.app.watchtime.data.models.Title
import com.app.watchtime.data.models.TitleType
import com.app.watchtime.ui.screens.HomeScreen
import com.app.watchtime.ui.viewmodel.home.HomeState
import com.app.watchtime.ui.viewmodel.home.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<HomeViewModel>()
    private val mockNavigateToTitle: (Int, TitleType) -> Unit = mockk(relaxed = true)

    @Test
    fun whenStateIsLoading_shouldShowShimmerEffect() {
        // Given
        val state = MutableStateFlow<HomeState>(HomeState.Loading)
        every { mockViewModel.state } returns state

        // When
        composeTestRule.setContent {
            HomeScreen(
                viewModel = mockViewModel,
                navigateToTitle = mockNavigateToTitle
            )
        }

        // Wait until shimmer nodes are rendered
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithContentDescription("Shimmer Effect").fetchSemanticsNodes().isNotEmpty()
        }

        // Then
        val shimmerNodes = composeTestRule.onAllNodesWithContentDescription("Shimmer Effect")
        val actualCount = shimmerNodes.fetchSemanticsNodes().size

        // Assert the count matches the expected behavior
        assert(actualCount == 6) { "Expected 6 shimmer nodes but found $actualCount." }
    }

    @Test
    fun whenStateIsFetched_shouldShowTitles() {
        // Given
        val movieTitles = listOf(
            Title(1, "Test Movie", "poster_url", 7.5),
            Title(2, "Another Movie", "poster_url2", 8.0)
        )
        val showTitles = listOf(
            Title(3, "Test Show", "poster_url3", 9.0)
        )
        val state = MutableStateFlow<HomeState>(
            HomeState.Fetched(
                movieTitles = movieTitles,
                showTitles = showTitles,
                isLoadingMore = false,
                loadingMoreError = null
            )
        )
        every { mockViewModel.state } returns state
        every { mockViewModel.loadMoreTitle(any()) } returns Unit // Mock loadMoreTitle

        // When
        composeTestRule.setContent {
            HomeScreen(
                viewModel = mockViewModel,
                navigateToTitle = mockNavigateToTitle
            )
        }

        // Then
        composeTestRule.onNodeWithText("Test Movie").assertExists()
        composeTestRule.onNodeWithText("Another Movie").assertExists()

        // Navigate to TV Shows tab
        composeTestRule.onNodeWithText("Shows").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Test Show").assertExists()
    }
    @Test
    fun whenStateIsError_shouldShowErrorMessage() {
        // Given
        val errorMessage = "An error occurred"
        val state = MutableStateFlow<HomeState>(HomeState.Error(errorMessage))
        every { mockViewModel.state } returns state

        // When
        composeTestRule.setContent {
            HomeScreen(
                viewModel = mockViewModel,
                navigateToTitle = mockNavigateToTitle
            )
        }

        // Then
        composeTestRule.onNodeWithText("No titles found").assertExists()
    }
}

