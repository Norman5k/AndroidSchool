package com.eltex.androidschool.repository

import android.content.ContentResolver
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class NetworkEventRepository @Inject constructor(
    private val eventsApi: EventsApi,
    private val mediaApi: MediaApi,
    private val contentResolver: ContentResolver
) : EventRepository {
    override suspend fun getLatest(count: Int): List<Event> = eventsApi.getLatest(count)

    override suspend fun getBefore(id: Long, count: Int): List<Event> = eventsApi.getBefore(id, count)

    override suspend fun participateById(id: Long): Event = eventsApi.participate(id)

    override suspend fun unparticipateById(id: Long): Event = eventsApi.unparticipate(id)

    override suspend fun likeById(id: Long): Event = eventsApi.like(id)

    override suspend fun dislikeById(id: Long): Event = eventsApi.dislike(id)
    override suspend fun saveEvent(id: Long, content: String, fileModel: FileModel?): Event {
        val event = fileModel?.let {
            val media = upload(it)

            eventsApi.save(Event(id, content, attachment = Attachment(media.url, it.attachmentType)))
        } ?: Event(id, content)

        return eventsApi.save(event)
    }

    private suspend fun upload(fileModel: FileModel): Media =
        mediaApi.upload(
            MultipartBody.Part.createFormData(
                "file",
                "file",
                withContext(Dispatchers.IO) {
                    requireNotNull(contentResolver.openInputStream(fileModel.uri)).use {
                        it.readBytes()
                    }
                        .toRequestBody()
                }
            )
        )

    override suspend fun deleteById(id: Long) = eventsApi.delete(id)


}