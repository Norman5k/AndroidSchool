package com.eltex.androidschool.model

import com.eltex.androidschool.utils.InstantSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Event(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("content")
    val content: String = "",
    @SerialName("author")
    val author: String = "",
    @SerialName("authorAvatar")
    val authorAvatar: String? = null,
    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("type")
    val type: String = "",
    @SerialName("datetime")
    @EncodeDefault
    @Serializable(InstantSerializer::class)
    val datetime: Instant = Instant.now(),
    @SerialName("link")
    val link: String = "",
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
    @SerialName("participantsIds")
    val participantsIds: Set<Long> = emptySet(),
    @SerialName("attachment")
    val attachment: Attachment? = null,
)
