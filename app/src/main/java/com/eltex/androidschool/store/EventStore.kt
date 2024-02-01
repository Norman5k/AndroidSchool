package com.eltex.androidschool.store

import com.eltex.androidschool.model.EventEffect
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.mvi.Store

typealias EventStore = Store<EventUiState, EventMessage, EventEffect>