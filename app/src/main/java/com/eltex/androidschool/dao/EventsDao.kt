package com.eltex.androidschool.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.eltex.androidschool.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT * FROM Events ORDER BY id DESC")
    fun getAll(): Flow<List<EventEntity>>
    @Upsert
    fun save(event: EventEntity): Long
    @Query(
        """
            UPDATE Events SET likedByMe =
            CASE WHEN likedByMe = 0 THEN 1 ELSE 0 END
            WHERE id = :eventId;
        """)
    fun likeById(eventId: Long)
    @Query(
        """
            UPDATE Events SET participatedByMe =
            CASE WHEN participatedByMe = 0 THEN 1 ELSE 0 END
            WHERE id = :eventId;
        """)
    fun participateById(eventId: Long)
    @Query("DELETE FROM Events WHERE id = :eventId")
    fun deleteById(eventId: Long)
    @Query("SELECT * FROM Events WHERE id = :eventId")
    fun getEventById(eventId: Long): EventEntity
    @Transaction
    fun updateById(id: Long, update: (EventEntity) -> EventEntity) {
        val event = getEventById(id)
        val updatedEvent = update(event)
        save(updatedEvent)
    }
}