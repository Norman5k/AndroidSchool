package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.TestEventUiModelMapper
import com.eltex.androidschool.TestSchedulersFactory
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import org.junit.Test

class EventViewModelTest {

    @Test
    fun `deleteById error then error in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.error(testError)

            },
            schedulersFactory = TestSchedulersFactory
        )

        viewModel.deleteById(1)

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `deleteById success then success in state`() {
        val testEvents: List<EventUiModel> = arrayListOf(EventUiModel(2))

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override fun getEvents(): Single<List<Event>> = Single.just(
                    arrayListOf(
                        Event(1),
                        Event(2)
                    )
                )

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.complete()

            },
            schedulersFactory = TestSchedulersFactory,
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
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.error(testError)

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory
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
                override fun getEvents(): Single<List<Event>> = Single.just(
                    arrayListOf(
                        Event(1),
                        Event(2)
                    )
                )

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> =
                    Single.just(Event(id, likedByMe = true))

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory,
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
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.error(testError)

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory
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
                override fun getEvents(): Single<List<Event>> = Single.just(
                    arrayListOf(
                        Event(1, likedByMe = true),
                        Event(2)
                    )
                )

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.just(Event(id, likedByMe = false))

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory,
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
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())

                override fun participateById(id: Long): Single<Event> = Single.error(testError)

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory
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
                override fun getEvents(): Single<List<Event>> = Single.just(
                    arrayListOf(
                        Event(1),
                        Event(2)
                    )
                )

                override fun participateById(id: Long): Single<Event> = Single.just(Event(1, participatedByMe = true))

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory,
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
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.error(testError)

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory
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
                override fun getEvents(): Single<List<Event>> = Single.just(
                    arrayListOf(
                        Event(1, participatedByMe = true),
                        Event(2)
                    )
                )

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.just(Event(1, participatedByMe = false))

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory,
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
                override fun getEvents(): Single<List<Event>> = Single.error(testError)

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory,
            mapper = TestEventUiModelMapper(),
        )

        assertEquals(testError, (viewModel.state.value.status as Status.Error).reason)
    }

    @Test
    fun `handle error with error then idle in state`() {
        val testError = RuntimeException("Test error")

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override fun getEvents(): Single<List<Event>> = Single.error(testError)

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory,
            mapper = TestEventUiModelMapper(),
        )

        viewModel.handleError()
        assertEquals(Status.Idle, viewModel.state.value.status)
    }

    @Test
    fun `handle error without error then idle in state`() {

        val viewModel = EventViewModel(
            repository = object : EventRepository {
                override fun getEvents(): Single<List<Event>> = Single.just(emptyList())

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.never()

            },
            schedulersFactory = TestSchedulersFactory,
            mapper = TestEventUiModelMapper(),
        )

        viewModel.handleError()
        assertEquals(Status.Idle, viewModel.state.value.status)
    }
}