package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event

interface EventRepository {
    suspend fun getLatest(count: Int): List<Event>
    suspend fun getBefore(id: Long, count: Int): List<Event>
    suspend fun participateById(id: Long): Event
    suspend fun unparticipateById(id: Long): Event
    suspend fun likeById(id: Long): Event
    suspend fun dislikeById(id: Long): Event
    suspend fun saveEvent(id: Long, content: String): Event
    suspend fun deleteById(id: Long)
}