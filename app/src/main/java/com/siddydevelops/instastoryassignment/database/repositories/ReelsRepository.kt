package com.siddydevelops.instastoryassignment.database.repositories

import com.siddydevelops.instastoryassignment.database.ReelsDatabase
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem

class ReelsRepository(
    private val db: ReelsDatabase
) {
    suspend fun insert(item: ReelsItem) = db.getReelsDAO().insert(item)

    suspend fun delete(item: ReelsItem) = db.getReelsDAO().delete(item)

    suspend fun update(item: ReelsItem) = db.getReelsDAO().update(item.video_uri,item.isLiked)

    fun getAllReels() = db.getReelsDAO().getAllReels()
}