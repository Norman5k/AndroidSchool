package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Status

data class NewEventUiState(
    val status : Status = Status.Idle,
    val result: Event? = null,
    val file: FileModel? = null,
)