package com.aarti.ai.data   // <- your database package

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aarti.ai.data.money.MoneyEntry   // <- MUST match the real package

@Database(entities = [MoneyEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // your DAOs
}