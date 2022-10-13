package com.siddydevelops.instastoryassignment.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem

@Dao
interface ReelsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ReelsItem)

    @Query("UPDATE reels_item SET video_uri=:newAmount WHERE isLiked = :itemName")
    suspend fun update(video_uri: String, isLiked: Boolean)

    @Delete
    suspend fun delete(item: ReelsItem)

    @Query("SELECT * FROM reels_item")
    fun getAllReels(): LiveData<List<ReelsItem>>

}