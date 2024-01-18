package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.MainDispatcherRule
import com.eltex.androidschool.TestEventUiModelMapper
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class EventViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `deleteById error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = throw testError
            }
        )

        viewModel.deleteById(1)

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `deleteById success then success in state`() {
        val testEvents: List<EventUiModel> = arrayListOf(EventUiModel(2))

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = arrayListOf(
                    Event(1),
                    Event(2)
                )

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) {}

            },
            mapper = TestEventUiModelMapper(),
        )

        viewModel.deleteById(1)

        assertEquals(testEvents, viewModel.state.value.events)
        assertEquals(Status.Idle, viewModel.state.value.status)
    }

    @Test
    fun `like error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = throw testError

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
        )

        viewModel.like(EventUiModel(likedByMe = false))

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `like success then success in state`() {
        val testEvents = arrayListOf<EventUiModel>(
            EventUiModel(1, likedByMe = true),
            EventUiModel(2)
        )

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> =
                    arrayListOf(
                        Event(1),
                        Event(2)
                    )

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = Event(id, likedByMe = true)

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
            mapper = TestEventUiModelMapper(),
        )

        viewModel.like(EventUiModel(1, likedByMe = false))

        assertEquals(testEvents, viewModel.state.value.events)
        assertEquals(Status.Idle, viewModel.state.value.status)
    }

    @Test
    fun `dislike error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = throw testError

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
        )

        viewModel.like(EventUiModel(likedByMe = true))

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `dislike success then success in state`() {
        val testEvents = arrayListOf<EventUiModel>(
            EventUiModel(1),
            EventUiModel(2)
        )

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = arrayListOf(
                    Event(1, likedByMe = true),
                    Event(2)
                )

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = Event(id, likedByMe = false)

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
            mapper = TestEventUiModelMapper(),
        )

        viewModel.like(EventUiModel(1, likedByMe = true))

        assertEquals(testEvents, viewModel.state.value.events)
        assertEquals(Status.Idle, viewModel.state.value.status)
    }

    @Test
    fun `participate error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun participateById(id: Long): Event = throw testError

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
        )

        viewModel.participate(EventUiModel(participatedByMe = false))

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `participate success then success in state`() {
        val testEvents = arrayListOf<EventUiModel>(
            EventUiModel(1, participatedByMe = true),
            EventUiModel(2)
        )

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = arrayListOf(
                    Event(1),
                    Event(2)
                )

                override suspend fun participateById(id: Long): Event =
                    Event(1, participatedByMe = true)

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
            mapper = TestEventUiModelMapper(),
        )

        viewModel.participate(EventUiModel(1, participatedByMe = false))

        assertEquals(testEvents, viewModel.state.value.events)
        assertEquals(Status.Idle, viewModel.state.value.status)
    }

    @Test
    fun `unparticipate error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = throw testError

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
        )

        viewModel.participate(EventUiModel(participatedByMe = true))

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `unparticipate success then success in state`() {
        val testEvents = arrayListOf<EventUiModel>(
            EventUiModel(1, participatedByMe = false),
            EventUiModel(2)
        )

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = arrayListOf(
                    Event(1, participatedByMe = true),
                    Event(2)
                )

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event =
                    Event(1, participatedByMe = false)

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
            mapper = TestEventUiModelMapper(),
        )

        viewModel.participate(EventUiModel(1, participatedByMe = true))

        assertEquals(testEvents, viewModel.state.value.events)
        assertEquals(Status.Idle, viewModel.state.value.status)
    }

    @Test
    fun `load error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = throw testError

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")

            },
            mapper = TestEventUiModelMapper(),
        )

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `handle error with error then idle in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> =throw testError

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")


            },
            mapper = TestEventUiModelMapper(),
        )

        viewModel.handleError()
        assertEquals(Status.Idle, viewModel.state.value.status)
    }

    @Test
    fun `handle error without error then idle in state`() {

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override suspend fun getEvents(): List<Event> = emptyList()

                override suspend fun participateById(id: Long): Event = error("Not implemented")

                override suspend fun unparticipateById(id: Long): Event = error("Not implemented")

                override suspend fun likeById(id: Long): Event = error("Not implemented")

                override suspend fun dislikeById(id: Long): Event = error("Not implemented")

                override suspend fun saveEvent(id: Long, content: String): Event =
                    error("Not implemented")

                override suspend fun deleteById(id: Long) = error("Not implemented")


            },
            mapper = TestEventUiModelMapper(),
        )

        viewModel.handleError()
        assertEquals(Status.Idle, viewModel.state.value.status)
    }
}