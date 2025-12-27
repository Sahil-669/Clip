package com.example.clip.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Query("SELECT * FROM Clip ORDER BY timestamp DESC")
    fun getAllClips(): Flow<List<Clip>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clip: Clip)

    @Delete
    suspend fun delete(clip: Clip)

    @Query("DELETE FROM Clip WHERE isPinned = 0")
    suspend fun clearUnpinned()
}