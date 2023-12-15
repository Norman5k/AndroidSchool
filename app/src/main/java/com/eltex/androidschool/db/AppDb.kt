package com.eltex.androidschool.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eltex.androidschool.dao.EventsDao
import com.eltex.androidschool.entity.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 1,
)
abstract class AppDb : RoomDatabase() {
    abstract val eventsDao : EventsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb {
            INSTANCE?.let {
                return it
            }

            val applicationContext = context.applicationContext

            synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val appDb = Room.databaseBuilder(applicationContext, AppDb::class.java, "app_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = appDb

                return appDb
            }
        }
    }
}