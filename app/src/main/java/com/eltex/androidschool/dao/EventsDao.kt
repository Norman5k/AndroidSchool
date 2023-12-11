package com.eltex.androidschool.dao

import com.eltex.androidschool.model.Event

interface EventsDao {
    fun getAll(): List<Event>
    fun save(event: Event): Event
    fun likeById(eventId: Long): Event
    fun participateById(eventId: Long): Event
    fun deleteById(eventId: Long)
    fun getEventById(eventId: Long): Event

}