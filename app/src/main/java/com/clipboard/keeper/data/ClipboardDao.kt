package com.clipboard.keeper.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

data class ClipboardPreview(
    val id: Int,
    val preview: String,
    val timestamp: Long
)

@Dao
interface ClipboardDao {
    @Query("SELECT id, substr(content, 1, 200) as preview, timestamp FROM clips ORDER BY timestamp DESC")
    fun getAllPreviews(): Flow<List<ClipboardPreview>>
    
    @Query("SELECT content FROM clips WHERE id = :id")
    suspend fun getFullContent(id: Int): String
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: ClipboardEntry)
    
    @Query("DELETE FROM clips WHERE id = :id")
    suspend fun deleteById(id: Int)
    
    @Query("DELETE FROM clips")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM clips")
    suspend fun getCount(): Int
}
