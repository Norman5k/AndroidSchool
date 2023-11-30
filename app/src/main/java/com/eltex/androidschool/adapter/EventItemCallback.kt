package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.model.Event

class EventItemCallback : ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: Event, newItem: Event): Any? =
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