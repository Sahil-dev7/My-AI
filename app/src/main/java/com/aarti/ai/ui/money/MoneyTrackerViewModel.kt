import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoneyTrackerViewModel(private val repo: MoneyRepository) : ViewModel() {

    private val _entries = MutableStateFlow<List<MoneyEntry>>(emptyList())
    val entries: StateFlow<List<MoneyEntry>> = _entries

    init {
        loadEntries()
    }

    fun addEntry(amount: Double, category: String, note: String?) {
        viewModelScope.launch {
            repo.addEntry(amount, category, note)
            loadEntries()
        }
    }

    fun deleteEntry(entry: MoneyEntry) {
        viewModelScope.launch {
            repo.deleteEntry(entry)
            loadEntries()
        }
    }

    private fun loadEntries() {
        viewModelScope.launch {
            _entries.value = repo.getAllEntries()
        }
    }
}