package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper = EventUiModelMapper.DEFAULT,
) : ViewModel() {

    private val _state = MutableStateFlow(EventUiState())
    val state: StateFlow<EventUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val events = repository.getEvents().map {
                    mapper.map(it)
                }

                _state.update {
                    it.copy(
                        events = events,
                        status = Status.Idle
                    )
                }
            } catch (e: Exception) {
                onError(e)
            }

        }
    }

    fun like(event: EventUiModel) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val result = if (!event.likedByMe) {
                    repository.likeById(event.id)
                } else {
                    repository.dislikeById(event.id)
                }
                val uiModel = mapper.map(result)

                onSuccess(uiModel)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun participate(event: EventUiModel) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val result = if (!event.participatedByMe) {
                    repository.participateById(event.id)
                } else {
                    repository.unparticipateById(event.id)
                }
                val uiModel = mapper.map(result)

                onSuccess(uiModel)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun deleteById(id: Long) {
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                repository.deleteById(id)

                _state.update { state ->
                    state.copy(
                        events = state.events.orEmpty()
                            .filter { it.id != id },
                        status = Status.Idle
                    )
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    private fun onSuccess(
        uiModel: EventUiModel
    ) {
        _state.update { state ->
            state.copy(
                events = state.events.orEmpty().map {
                    if (it.id == uiModel.id) {
                        uiModel
                    } else {
                        it
                    }
                },
                status = Status.Idle
            )
        }
    }

    private fun onError(e: Exception) {
        _state.update {
            it.copy(status = Status.Error(e))
        }
    }

    fun handleError() {
        _state.update {
            if (it.status is Status.Error) {
                it.copy(status = Status.Idle)
            } else {
                it
            }

        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}