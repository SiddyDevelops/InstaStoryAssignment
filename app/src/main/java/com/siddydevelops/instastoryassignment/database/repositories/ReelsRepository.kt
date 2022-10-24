package com.siddydevelops.instastoryassignment.database.repositories

import com.siddydevelops.instastoryassignment.database.ReelsDatabase
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem

class ReelsRepository(
    private val db: ReelsDatabase
) {
    suspend fun insert(item: ReelsItem) = db.getReelsDAO().insert(item)

    suspend fun delete(item: ReelsItem) = db.getReelsDAO().delete(item)

    suspend fun updateLike(item: ReelsItem) = db.getReelsDAO().updateLike(item.video_uri,item.isLiked)

    suspend fun update(item: ReelsItem) = db.getReelsDAO().update(item)

    fun getAllReels() = db.getReelsDAO().getAllReels()
}