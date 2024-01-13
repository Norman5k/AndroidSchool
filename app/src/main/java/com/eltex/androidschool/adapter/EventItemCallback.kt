package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.model.EventUiModel

class EventItemCallback : ItemCallback<EventUiModel>() {
    override fun areItemsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: EventUiModel, newItem: EventUiModel): Any? =
        EventPayLoad(
            liked = newItem.likedByMe.takeIf {
                it != oldItem.likedByMe
            },
            participated = newItem.participatedByMe.takeIf {
                it != oldItem.participatedByMe
            }
        )
            .takeIf {
                it.isNotEmpty()
            }
}