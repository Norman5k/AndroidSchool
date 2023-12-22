package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.repository.EventRepository

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long,
) : ViewModel() {

    fun save(content: String) {
        if (id != 0L) {
            repository.editById(id, content)
        } else {
            repository.saveEvent(id, content)
        }

    }
}