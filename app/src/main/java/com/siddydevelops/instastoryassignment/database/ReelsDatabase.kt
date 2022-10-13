package com.siddydevelops.instastoryassignment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem

@Database(
    entities = [ReelsItem::class],
    version = 1
)
abstract class ReelsDatabase : RoomDatabase() {
    abstract fun getReelsDAO(): ReelsDAO

    companion object {
        @Volatile
        private var instance: ReelsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ReelsDatabase::class.java,"ReelsDB.db").build()
    }
}