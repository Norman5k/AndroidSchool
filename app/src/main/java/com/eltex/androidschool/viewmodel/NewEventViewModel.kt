package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long,
) : ViewModel() {

    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()
    fun save(content: String) {
        _state.update { it.copy(status = Status.Loading) }

        if (id != 0L) {
            //repository.editById(id, content)
        } else {
            repository.saveEvent(id, content, object : Callback<Event> {
                override fun onSuccess(data: Event) {
                    _state.update {
                        it.copy(
                            status = Status.Idle,
                            result = data
                        )
                    }
                }

                override fun onError(throwable: Throwable) {
                    _state.update { it.copy(status = Status.Error(throwable)) }
                }

            })
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
}