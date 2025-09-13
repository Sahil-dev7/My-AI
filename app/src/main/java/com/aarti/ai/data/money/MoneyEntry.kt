package com.aarti.ai.data.MoneyEntry


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "money_entries")
data class MoneyEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val category: String,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)