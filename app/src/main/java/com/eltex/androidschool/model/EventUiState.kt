package com.eltex.androidschool.model

data class EventUiState(
    val events: List<EventUiModel> = emptyList(),
    val status: EventStatus = EventStatus.Idle,
    val singleError: Throwable? = null,
) {
    val isRefreshing: Boolean = status == EventStatus.Refreshing

    val isEmptyLoading: Boolean = status == EventStatus.InitialLoading

    val emptyError: Throwable? = (status as? EventStatus.EmptyError)?.reason

}