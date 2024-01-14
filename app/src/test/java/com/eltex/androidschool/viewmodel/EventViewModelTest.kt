package com.eltex.androidschool.viewmodel

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
                override fun getEvents(): Single<List<Event>> = Single.just(arrayListOf(Event(1), Event(2)))

                override fun participateById(id: Long): Single<Event> = Single.never()

                override fun unparticipateById(id: Long): Single<Event> = Single.never()

                override fun likeById(id: Long): Single<Event> = Single.never()

                override fun dislikeById(id: Long): Single<Event> = Single.never()

                override fun saveEvent(id: Long, content: String): Single<Event> = Single.never()

                override fun deleteById(id: Long): Completable = Completable.complete()

            },
            schedulersFactory = TestSchedulersFactory
        )

        viewModel.deleteById(1)

        assertEquals(testEvents, (viewModel.state.value.events))
    }
}