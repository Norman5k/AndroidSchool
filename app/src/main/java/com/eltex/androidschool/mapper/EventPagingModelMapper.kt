package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.EventStatus
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.model.PagingModel

class EventPagingModelMapper {
    fun map(eventUiState: EventUiState): List<PagingModel<EventUiModel>> {
        val events = eventUiState.events.map {
            PagingModel.Data(it)
        }

        return when (val status = eventUiState.status) {
            is EventStatus.NextPageError -> events + PagingModel.Error(status.reason)
            EventStatus.InitialLoading -> events + eventUiState.skeletons.map {
                PagingModel.Progress(it)
            }
            EventStatus.NextPageLoading -> events + eventUiState.skeletons.map {
                PagingModel.Progress(it)
            }

            else -> events
        }
    }
}