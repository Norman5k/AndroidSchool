package com.eltex.androidschool.adapter

import com.eltex.androidschool.model.Attachment

data class EventPayLoad(
    val liked: Boolean? = null,
    val likes: Int? = null,
    val participated: Boolean? = null,
    val participants: Int? = null,
    val content: String? = null,
    val attachment: Attachment? = null,
    val authorAvatar: String? = null,
) {
    fun isNotEmpty(): Boolean = (liked != null)
            || (likes != null)
            || (participated != null)
            || (participants != null)
            || (content != null)
            || (attachment != null)
            || (authorAvatar != null)
}
