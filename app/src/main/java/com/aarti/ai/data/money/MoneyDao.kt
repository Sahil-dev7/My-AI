class MoneyRepository(private val dao: MoneyDao) {

    suspend fun addEntry(amount: Double, category: String, note: String?) {
        val entry = MoneyEntry(amount = amount, category = category, note = note)
        dao.insert(entry)
    }

    suspend fun deleteEntry(entry: MoneyEntry) {
        dao.delete(entry)
    }

    suspend fun getAllEntries(): List<MoneyEntry> = dao.getAll()
}