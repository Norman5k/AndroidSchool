package com.eltex.androidschool.adapter

data class EventPayLoad(
    val liked: Boolean? = null,
    val likes: Int? = null,
    val participated: Boolean? = null,
    val participants: Int? = null,
) {
    fun isNotEmpty(): Boolean = (liked != null)
            || (likes != null)
            || (participated != null)
            || (participants != null)
}
