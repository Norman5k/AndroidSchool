package com.eltex.androidschool.dao

object EventsTable {
    const val TABLE_NAME = "Events"
    const val ID = "id"
    const val CONTENT = "content"
    const val AUTHOR = "author"
    const val PUBLISHED = "published"
    const val EVENT_TYPE = "eventType"
    const val EVENT_DATE = "eventDate"
    const val LINK = "link"
    const val LIKED_BY_ME = "likedByMe"
    const val PARTICIPATED_BY_ME = "participatedByMe"
    val allColumns = arrayOf(
        ID, CONTENT, AUTHOR, PUBLISHED,
        EVENT_TYPE, EVENT_DATE, LINK,
        LIKED_BY_ME, PARTICIPATED_BY_ME
    )
}