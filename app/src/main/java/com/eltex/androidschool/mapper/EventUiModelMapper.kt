package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventUiModelMapper {
    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }
    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            content = content,
            author = author,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            type = type,
            datetime = FORMATTER.format(datetime.atZone(ZoneId.systemDefault())),
            link = link,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            participatedByMe = participatedByMe,
            participants = participantsIds.size,
        )
    }
}