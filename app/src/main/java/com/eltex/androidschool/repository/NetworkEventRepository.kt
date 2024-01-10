package com.eltex.androidschool.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.Callback
import retrofit2.Call
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class NetworkEventRepository(
    private val api: EventsApi
) : EventRepository {

    override fun getEvents(callback: Callback<List<Event>>) {
        api.getAll().enqueue(
            object : retrofit2.Callback<List<Event>> {
                override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(
                            RuntimeException("Response code: ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    callback.onError(t)
                }

            }
        )
    }

    override fun participateById(id: Long, callback: Callback<Event>) {
        api.participate(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(
                            RuntimeException("Response code: ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun unparticipateById(id: Long, callback: Callback<Event>) {
        api.unparticipate(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(
                            RuntimeException("Response code: ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun likeById(id: Long, callback: Callback<Event>) {
        api.like(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(
                            RuntimeException("Response code: ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun dislikeById(id: Long, callback: Callback<Event>) {
        api.dislike(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(
                            RuntimeException("Response code: ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun saveEvent(
        id: Long,
        content: String,
        callback: Callback<Event>
    ) {
        val timeStamp: String = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        api.save(Event(id, content, datetime = timeStamp)).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(
                            RuntimeException("Response code: ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun deleteById(id: Long, callback: Callback<Unit>) {
        api.delete(id).enqueue(
            object : retrofit2.Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(
                            RuntimeException("Response code: ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(t)
                }

            }
        )
    }
}