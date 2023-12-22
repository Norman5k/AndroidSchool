package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.EventsDao
import com.eltex.androidschool.entity.EventEntity
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SQLiteEventRepository(private val dao: EventsDao) : EventRepository {
    override fun getEvents(): Flow<List<Event>> = dao.getAll()
        .map {
            it.map(EventEntity::toEvent)
        }

    override fun participateById(id: Long) {
        dao.participateById(id)
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun saveEvent(id: Long, content: String) {
        dao.save(
            EventEntity.fromEvent(
                Event(id = id, content = content)
            )
        )
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

   override fun editById(id: Long, content: String) {
        dao.updateById(id) { it.copy(content = content)}
    }
}