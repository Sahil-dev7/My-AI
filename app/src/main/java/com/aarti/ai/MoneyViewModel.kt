package com.aarti.ai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoneyViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).moneyEntryDao()
    private val _entries = MutableStateFlow<List<MoneyEntry>>(emptyList())
    val entries: StateFlow<List<MoneyEntry>> = _entries

    init {
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            _entries.value = dao.getAll()
        }
    }

    fun addEntry(amount: Double, description: String) {
        viewModelScope.launch {
            dao.insert(MoneyEntry(amount = amount, description = description))
            loadEntries()
        }
    }

    fun deleteEntry(entry: MoneyEntry) {
        viewModelScope.launch {
            dao.delete(entry)
            loadEntries()
        }
    }

    fun updateEntry(entry: MoneyEntry) {
        viewModelScope.launch {
            dao.update(entry)
            loadEntries()
        }
    }
}
