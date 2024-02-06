package com.eltex.androidschool.feature.events

import com.eltex.androidschool.model.EventUiModel

interface EventListener {
    fun onLikeClickListener(event: EventUiModel)
    fun onShareClickListener(event: EventUiModel)
    fun onDeleteClickListener(event: EventUiModel)
    fun onEditClickListener(event: EventUiModel)
    fun onParticipateClickListener(event: EventUiModel)
    fun onRetryClickListener()
    fun onRefresh()
    fun loadNextPage()
}