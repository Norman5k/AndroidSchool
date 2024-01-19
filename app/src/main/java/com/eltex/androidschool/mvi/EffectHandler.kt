package com.eltex.androidschool.mvi

import kotlinx.coroutines.flow.Flow

interface EffectHandler<Effect, Message> {
    fun connect(messages: Flow<Effect>): Flow<Message>
}