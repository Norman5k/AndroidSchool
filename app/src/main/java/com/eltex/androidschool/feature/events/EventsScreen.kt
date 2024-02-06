package com.eltex.androidschool.feature.events

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.androidschool.R
import com.eltex.androidschool.di.DateTimeFormatter
import com.eltex.androidschool.effecthandler.EventEffectHandler
import com.eltex.androidschool.feature.paging.ItemError
import com.eltex.androidschool.feature.paging.ItemProgress
import com.eltex.androidschool.mapper.EventPagingModelMapper
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventStatus
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.reducer.EventReducer
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.store.EventStore
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.filter
import java.time.ZoneId

@Composable
fun EventsScreen(modifier: Modifier = Modifier, viewModel: EventViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val mapper = remember { EventPagingModelMapper() }
    val context = LocalContext.current

    val eventListener = object : EventListener {
        override fun onLikeClickListener(event: EventUiModel) {
            viewModel.accept(EventMessage.Like(event))
        }

        override fun onShareClickListener(event: EventUiModel) {
            val intent = Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, event.content)
                .setType("text/plain")

            val chooser = Intent.createChooser(intent, null)
            context.startActivity(chooser)
        }

        override fun onDeleteClickListener(event: EventUiModel) {
            viewModel.accept(EventMessage.Delete(event))
        }

        override fun onEditClickListener(event: EventUiModel) {
            /*requireParentFragment()
                .requireParentFragment()
                .findNavController()
                .navigate(
                    R.id.action_bottomNavigationFragment_to_editEventFragment,
                    bundleOf(
                        NewEventFragment.ARG_ID to event.id,
                        NewEventFragment.ARG_CONTENT to event.content,
                    )
                )*/
            // TODO
        }

        override fun onParticipateClickListener(event: EventUiModel) {
            viewModel.accept(EventMessage.Participate(event))
        }

        override fun onRetryClickListener() {
            viewModel.accept(EventMessage.Retry)
        }

        override fun onRefresh() {
            viewModel.accept(EventMessage.Refresh)
        }

        override fun loadNextPage() {
            viewModel.accept(EventMessage.LoadNextPage)
        }
    }

    when (val status = state.status) {
        is EventStatus.EmptyError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ItemError(error = status.reason) {
                    viewModel.accept(EventMessage.Refresh)
                }
            }
        }

        EventStatus.InitialLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ItemProgress(count = 30)
            }
        }

        else -> EventsListScreen(
            mapper.map(state),
            status is EventStatus.Refreshing,
            eventListener,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EventsListScreen(
    items: List<PagingModel<EventUiModel>>,
    refreshing: Boolean,
    listener: EventListener,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = refreshing, onRefresh = { listener.onRefresh() })

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            snapshotFlow {
                val position = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val itemsCount = listState.layoutInfo.totalItemsCount
                (position == itemsCount - 1) ||
                        (position == itemsCount - 4)
            }
                .filter { it }
                .collect {
                    listener.loadNextPage()
                }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            items(items, key = {
                when (it) {
                    is PagingModel.Data -> it.value.id.toString()
                    is PagingModel.Error -> PagingModel.Error::class.simpleName.orEmpty()
                    PagingModel.Progress -> PagingModel.Progress::class.simpleName.orEmpty()
                }
            }) { item ->
                when (item) {
                    is PagingModel.Data -> {
                        val event = item.value
                        CardEvent(
                            event = event,
                            onDeleteClickListener = { listener.onDeleteClickListener(event) },
                            onEditClickListener = { listener.onEditClickListener(event) },
                            onLikeClickListener = { listener.onLikeClickListener(event) },
                            onShareClickListener = { listener.onShareClickListener(event) },
                            onParticipateClickListener = {
                                listener.onParticipateClickListener(
                                    event
                                )
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    is PagingModel.Error -> ItemError(error = item.reason) {
                        listener.onRetryClickListener()
                    }

                    PagingModel.Progress -> ItemProgress(count = 10)
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing, state = pullRefreshState, Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}

@Composable
private fun EventsScreenPreview(state: EventUiState) {
    ComposeAppTheme {
        EventsScreen(
            viewModel = EventViewModel(
                EventStore(
                    EventReducer(),
                    EventEffectHandler(
                        object : EventRepository {},
                        EventUiModelMapper(DateTimeFormatter(ZoneId.systemDefault()))
                    ),
                    initialState = state
                )
            )
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EventScreenEmptyLoadingPreview() {
    EventsScreenPreview(state = EventUiState(emptyList(), status = EventStatus.InitialLoading))
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EventScreenEmptyErrorPreview(){
    EventsScreenPreview(
        state = EventUiState(
            emptyList(),
            status =  EventStatus.EmptyError(RuntimeException("Test"))
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EventScreenIdlePreview(){
    EventsScreenPreview(
        state = EventUiState(
            List(10) {
                EventUiModel(
                    content = stringResource(id = R.string.skeleton_content),
                    author = stringResource(id = R.string.skeleton_author),
                    published = stringResource(id = R.string.skeleton_published),
                    type = stringResource(id = R.string.skeleton_type),
                    datetime = stringResource(id = R.string.skeleton_datetime),
                    link = stringResource(id = R.string.skeleton_link),
                    likes = stringResource(id = R.string.skeleton_likes).toInt(),
                    likedByMe = true,
                    participants = stringResource(id = R.string.skeleton_participants).toInt(),
                )
            },
        )
    )
}