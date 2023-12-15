package com.eltex.androidschool.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.androidschool.model.Event

@Entity(tableName = "Events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "published")
    val published: String = "",
    @ColumnInfo(name = "eventType")
    val eventType: String = "",
    @ColumnInfo(name = "eventDate")
    val eventDate: String = "",
    @ColumnInfo(name = "link")
    val link: String = "",
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
    @ColumnInfo(name = "participatedByMe")
    val participatedByMe: Boolean = false,
) {
    fun toEvent(): Event = Event(
        id = id,
        content = content,
        author = author,
        published = published,
        eventType = eventType,
        eventDate = eventDate,
        link = link,
        likedByMe = likedByMe,
        participatedByMe = participatedByMe,
    )

    companion object {
        fun fromEvent(event: Event): EventEntity =
            with(event) {
                EventEntity(
                    id = id,
                    content = content,
                    author = author,
                    published = published,
                    eventType = eventType,
                    eventDate = eventDate,
                    link = link,
                    likedByMe = likedByMe,
                    participatedByMe = participatedByMe
                )
            }
    }
}
