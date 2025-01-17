package com.app.watchtime.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.watchtime.data.models.Title
import com.app.watchtime.data.models.TitleType
import com.app.watchtime.ui.composables.TabBar
import com.app.watchtime.ui.composables.TitleCoverCard
import com.app.watchtime.ui.viewmodel.home.HomeState
import com.app.watchtime.ui.viewmodel.home.HomeViewModel
import io.reactivex.rxjava3.internal.operators.maybe.MaybeIsEmpty
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(), navigateToTitle: (Int, TitleType) -> Unit
) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        pageCount = { 2 })
    var selectTab by remember { mutableIntStateOf(0) }

    selectTab = pagerState.currentPage

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            TabBar(selectTab) {
                coroutineScope.launch {
                    pagerState.scrollToPage(it)
                }
            }
            Spacer(Modifier.height(12.dp))

            HorizontalPager(
                state = pagerState
            ) { page ->

                val gridState = rememberLazyGridState()

                LaunchedEffect(gridState) {
                    snapshotFlow { gridState.isScrolledToEnd() }
                        .distinctUntilChanged()
                        .collect { isAtEnd ->
                            if(isAtEnd && state.value is HomeState.Fetched){
                                viewModel.loadMoreTitle(if(page == 0) TitleType.MOVIE else TitleType.TV)
                            }
                        }
                }


                TitlesPage(
                    gridState = gridState
                ) {
                    when (val homeState = state.value) {
                        is HomeState.Error -> {
                            item(span = { GridItemSpan(2) }) {
                                ErrorMessage(homeState.message, isEmpty = false)
                            }
                        }
                        is HomeState.Fetched -> {
                            val titles = if (page == 0) homeState.movieTitles else homeState.showTitles
                            items(titles.size) { index ->
                                TitleCard(titles[index]) {
                                    navigateToTitle(
                                        titles[index].id,
                                        if (page == 0) TitleType.MOVIE else TitleType.TV
                                    )
                                }
                            }

                            if (homeState.isLoadingMore) {
                                items(2) {
                                    TitleCard(isShimmer = true)
                                }
                            }
                        }

                        HomeState.Loading -> {
                            items(10) {
                                TitleCard(isShimmer = true)
                            }
                        }

                        HomeState.Empty -> {
                            item(span = { GridItemSpan(2) }) {
                                ErrorMessage()
                            }
                        }
                    }
                }
            }

        }
    }
}

private fun LazyGridState.isScrolledToEnd(): Boolean {
    val visibleItems = layoutInfo.visibleItemsInfo
    val totalItems = layoutInfo.totalItemsCount
    return visibleItems.isNotEmpty() && visibleItems.last().index == totalItems - 1
}

@Composable
fun TitlesPage(
    gridState: LazyGridState, content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.Center,
        content = content
    )
}

@Composable
fun TitleCard(
    title: Title? = null, isShimmer: Boolean = false, onTitleClick: () -> Unit = {}
) {
    Column(
        Modifier
            .padding(8.dp)
            .clickable {
                onTitleClick()
            }) {
        if (!isShimmer) {
            TitleCoverCard(
                imageUrl = title!!.poster
            )
            Spacer(Modifier.height(4.dp))
            Text(
                title.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        } else {
            TitleCoverCard(
                isShimmerEffect = true
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 32.dp)
    ) {
        Column {
            Text(
                "WatchTime", style = MaterialTheme.typography.titleLarge
            )
            Text(
                "Find Your Next Binge-Worthy Title Here.",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String = "No titles found",
    isEmpty: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isEmpty) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
        )
    }
}


