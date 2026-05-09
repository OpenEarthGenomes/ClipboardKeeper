package com.clipboard.keeper.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "clips")
data class ClipboardEntry(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)
