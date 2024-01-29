package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

interface EventUiModelMapper {
    fun map(event: Event): EventUiModel = with(event) {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
        EventUiModel(
            id = id,
            content = content,
            author = author,
            published = formatter.format(published.atZone(ZoneId.systemDefault())),
            type = type,
            datetime = formatter.format(datetime.atZone(ZoneId.systemDefault())),
            link = link,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            participatedByMe = participatedByMe,
            participants = participantsIds.size,
            attachment = attachment,
            authorAvatar = authorAvatar
        )
    }

    companion object {
        val DEFAULT = object : EventUiModelMapper {}
    }
}