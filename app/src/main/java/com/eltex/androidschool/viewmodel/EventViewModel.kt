package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.store.EventStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class EventViewModel @Inject constructor(
    private val store: EventStore
) : ViewModel() {

    val state: StateFlow<EventUiState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(eventMessage: EventMessage) {
        store.accept(eventMessage)
    }
}