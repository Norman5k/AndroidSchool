package com.eltex.androidschool.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("content")
    val content: String = "",
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    val published: String = "",
    @SerialName("eventType")
    val eventType: String = "",
    @SerialName("eventDate")
    val eventDate: String = "",
    @SerialName("link")
    val link: String = "",
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
)
