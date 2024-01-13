package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long,
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()
    fun save(content: String) {
        _state.update { it.copy(status = Status.Loading) }

        repository.saveEvent(id, content)
            .subscribeBy(
                onSuccess = {data->
                    _state.update {
                        it.copy(
                            status = Status.Idle,
                            result = data
                        )
                    }
                },
                onError = {throwable->
                    _state.update { it.copy(status = Status.Error(throwable)) }
                }
            )
            .addTo(disposable)
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
        disposable.dispose()
    }
}