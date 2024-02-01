package com.eltex.androidschool.mapper

import com.eltex.androidschool.di.DateTimeFormatter
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import javax.inject.Inject


class EventUiModelMapper @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter
) {
    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            content = content,
            author = author,
            published = dateTimeFormatter.format(published),
            type = type,
            datetime = dateTimeFormatter.format(datetime),
            link = link,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            participatedByMe = participatedByMe,
            participants = participantsIds.size,
            attachment = attachment,
            authorAvatar = authorAvatar
        )
    }
}