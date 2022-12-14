package com.siddydevelops.instastoryassignment.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem

@Dao
interface ReelsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ReelsItem)

    @Query("UPDATE reels_item SET isLiked = :isLiked WHERE video_uri=:video_uri")
    suspend fun updateLike(video_uri: String, isLiked: Boolean)

    @Update
    suspend fun update(item: ReelsItem)

    @Delete
    suspend fun delete(item: ReelsItem)

    @Query("SELECT * FROM reels_item")
    fun getAllReels(): LiveData<List<ReelsItem>>

}