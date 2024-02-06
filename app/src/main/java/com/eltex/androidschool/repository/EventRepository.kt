package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.FileModel

interface EventRepository {
    suspend fun getLatest(count: Int): List<Event> = error("Not implemented")
    suspend fun getBefore(id: Long, count: Int): List<Event> = error("Not implemented")
    suspend fun participateById(id: Long): Event = error("Not implemented")
    suspend fun unparticipateById(id: Long): Event = error("Not implemented")
    suspend fun likeById(id: Long): Event = error("Not implemented")
    suspend fun dislikeById(id: Long): Event = error("Not implemented")
    suspend fun saveEvent(id: Long, content: String, fileModel: FileModel?): Event =
        error("Not implemented")

    suspend fun deleteById(id: Long): Unit = error("Not implemented")
}