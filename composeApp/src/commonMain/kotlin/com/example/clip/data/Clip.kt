package com.example.clip.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock


@Entity
data class Clip(
    @PrimaryKey val id: String,
    val content: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val isPinned: Boolean = false,
    val type: ClipType = ClipType.TEXT
)

enum class ClipType {
    TEXT, LINK, COLOR, CODE, IMAGE
}

