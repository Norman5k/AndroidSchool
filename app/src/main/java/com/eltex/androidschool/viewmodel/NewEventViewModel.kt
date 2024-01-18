package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long,
) : ViewModel() {
    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()
    fun save(content: String) {
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val result = repository.saveEvent(id, content)
                _state.update {
                    it.copy(
                        status = Status.Idle,
                        result = result
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = Status.Error(e)) }
            }
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