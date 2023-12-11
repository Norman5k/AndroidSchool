package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import com.eltex.androidschool.model.Event

class EventsDaoImpl(private val db: SQLiteDatabase) : EventsDao {
    override fun getAll(): List<Event> =
        db.query(
            EventsTable.TABLE_NAME,
            EventsTable.allColumns,
            null,
            null,
            null,
            null,
            "${EventsTable.ID} DESC"
        ).use { cursor ->
            val result = mutableListOf<Event>()

            while (cursor.moveToNext()) {
                result.add(cursor.getEvent())
            }

            result
        }


    override fun save(event: Event): Event {
        val contentValues = contentValuesOf(
            EventsTable.CONTENT to event.content,
            EventsTable.AUTHOR to event.author,
            EventsTable.PUBLISHED to event.published,
            EventsTable.EVENT_TYPE to event.eventType,
            EventsTable.EVENT_DATE to event.eventDate,
            EventsTable.LINK to event.link,
            EventsTable.LIKED_BY_ME to event.likedByMe,
            EventsTable.PARTICIPATED_BY_ME to event.participatedByMe,
        )

        if (event.id != 0L) {
            contentValues.put(EventsTable.ID, event.id)
        }

        val eventId = db.replace(EventsTable.TABLE_NAME, null, contentValues)

        return getEventById(eventId)
    }

    override fun likeById(eventId: Long): Event {
        db.execSQL("""
            UPDATE ${EventsTable.TABLE_NAME} SET ${EventsTable.LIKED_BY_ME} =
            CASE WHEN ${EventsTable.LIKED_BY_ME} = 0 THEN 1 ELSE 0 END
            WHERE ${EventsTable.ID} = $eventId;
        """.trimIndent())

        return getEventById(eventId)
    }

    override fun participateById(eventId: Long): Event {
        db.execSQL("""
            UPDATE ${EventsTable.TABLE_NAME} SET ${EventsTable.PARTICIPATED_BY_ME} =
            CASE WHEN ${EventsTable.PARTICIPATED_BY_ME} = 0 THEN 1 ELSE 0 END
            WHERE ${EventsTable.ID} = $eventId;
        """.trimIndent())

        return getEventById(eventId)
    }

    override fun deleteById(eventId: Long) {
        db.delete(
            EventsTable.TABLE_NAME,
            "${EventsTable.ID} = ?",
            arrayOf(eventId.toString())
        )
    }

    private fun Cursor.getEvent(): Event =
        Event(
            id = getLongOrThrow(EventsTable.ID),
            content = getStringOrThrow(EventsTable.CONTENT),
            author = getStringOrThrow(EventsTable.AUTHOR),
            published = getStringOrThrow(EventsTable.PUBLISHED),
            eventType = getStringOrThrow(EventsTable.EVENT_TYPE),
            eventDate = getStringOrThrow(EventsTable.EVENT_DATE),
            link = getStringOrThrow(EventsTable.LINK),
            likedByMe = getBooleanOrThrow(EventsTable.LIKED_BY_ME),
            participatedByMe = getBooleanOrThrow(EventsTable.PARTICIPATED_BY_ME),
        )

    fun Cursor.getLongOrThrow(columnName: String): Long = getLong(getColumnIndexOrThrow(columnName))

    fun Cursor.getStringOrThrow(columnName: String): String = getString(getColumnIndexOrThrow(columnName))

    fun Cursor.getIntOrThrow(columnName: String): Int = getInt(getColumnIndexOrThrow(columnName))

    fun Cursor.getBooleanOrThrow(columnName: String): Boolean = getIntOrThrow(columnName) != 0

    override fun getEventById(eventId: Long): Event =
        db.query(
            EventsTable.TABLE_NAME,
            EventsTable.allColumns,
            "${EventsTable.ID} = ?",
            arrayOf(eventId.toString()),
            null,
            null,
            null
        ).use { cursor ->
            cursor.moveToNext()

            cursor.getEvent()
        }

}