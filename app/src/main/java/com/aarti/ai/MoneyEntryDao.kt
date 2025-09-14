package com.aarti.ai

import androidx.room.*

@Dao
interface MoneyEntryDao {
    @Query("SELECT * FROM money_entries ORDER BY timestamp DESC")
    suspend fun getAll(): List<MoneyEntry>

    @Insert
    suspend fun insert(entry: MoneyEntry)

    @Delete
    suspend fun delete(entry: MoneyEntry)

    @Update
    suspend fun update(entry: MoneyEntry)
}
