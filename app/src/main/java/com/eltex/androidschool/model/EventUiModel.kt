package com.eltex.androidschool.model

data class EventUiModel(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val published: String = "",
    val type: String = "",
    val datetime: String = "",
    val link: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val participatedByMe: Boolean = false,
    val participants: Int = 0,
)