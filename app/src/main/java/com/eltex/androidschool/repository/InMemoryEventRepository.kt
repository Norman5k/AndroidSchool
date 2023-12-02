package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryEventRepository : EventRepository {
    private val state = MutableStateFlow(
        List(100) {
            Event(
                id = it.toLong() + 1L,
                content = "№$it Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                eventType = "Offline",
                eventDate = "16.05.22 12:00",
                link = "https://m2.material.io/components/cards",
                likedByMe = false,
                participatedByMe = false,
            )
        }
            .reversed()
    )
    private var nextId = state.value.first().id

    override fun getEvents(): Flow<List<Event>> = state.asStateFlow()

    override fun participateById(id: Long) {
        state.update {
            it.map { event ->
                if (event.id == id) {
                    event.copy(participatedByMe = !event.participatedByMe)
                } else {
                    event
                }
            }
        }
    }

    override fun likeById(id: Long) {
        state.update {
            it.map { event ->
                if (event.id == id) {
                    event.copy(likedByMe = !event.likedByMe)
                } else {
                    event
                }
            }
        }
    }

    override fun addEvent(content: String) {
        state.update { events ->
            buildList(events.size + 1) {
                add(
                    Event(
                        id = ++nextId,
                        content = content,
                        author = "User",
                        published = "Now",
                        eventType = "Offline",
                        eventDate = "Tomorrow",
                        link = "https://m2.material.io/components/cards"
                    )
                )
                addAll(events)
            }
        }
    }

    override fun deleteById(id: Long) {
        state.update { events ->
            events.filter {
                it.id != id
            }
        }
    }

    override fun editById(id: Long, content: String) {
        state.update { events ->
            events.map { event ->
                if (event.id == id) {
                    event.copy(content = content)
                } else {
                    event
                }
            }
        }
    }
}