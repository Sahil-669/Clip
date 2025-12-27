package com.example.clip.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromClipType(value: String): ClipType {
        return try {
            ClipType.valueOf(value)
        } catch (_: Exception) {
            ClipType.TEXT
        }
    }

    @TypeConverter
    fun clipTypeToString(type: ClipType): String {
        return type.name
    }

}