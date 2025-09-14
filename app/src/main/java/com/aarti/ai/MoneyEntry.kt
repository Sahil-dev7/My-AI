package com.aarti.ai

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "money_entries")
data class MoneyEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
