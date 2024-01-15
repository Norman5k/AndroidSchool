package com.eltex.androidschool

import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel

class TestEventUiModelMapper : EventUiModelMapper {
    override fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            content = content,
            author = author,
            published = "",
            type = type,
            datetime = "",
            link = link,
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            participatedByMe = participatedByMe,
            participants = participantsIds.size,
        )
    }
}