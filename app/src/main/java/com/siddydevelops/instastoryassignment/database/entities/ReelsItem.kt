package com.siddydevelops.instastoryassignment.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "reels_item",indices = [Index(value = ["video_uri"], unique = true)])
data class ReelsItem(
    @PrimaryKey
    @ColumnInfo(name = "video_uri")
    val video_uri: String,
    @ColumnInfo(name = "isLiked")
    var isLiked: Boolean
)