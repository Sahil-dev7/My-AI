package com.yourapp.ui.money

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.yourapp.data.db.AppDatabase
import com.yourapp.data.money.MoneyRepository

class MoneyTrackerScreenActivity : ComponentActivity() {

    private val viewModel: MoneyTrackerViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "aarti_app_db"
                ).build()
                val repo = MoneyRepository(db.moneyDao())
                @Suppress("UNCHECKED_CAST")
                return MoneyTrackerViewModel(repo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyTrackerScreen(viewModel = viewModel)
        }
    }
}