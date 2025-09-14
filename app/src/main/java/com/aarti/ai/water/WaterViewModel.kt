package com.aarti.ai.water

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WaterEntry(
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)

class WaterViewModel(application: Application) : AndroidViewModel(application) {
    private val appContext = application.applicationContext
    private val prefs = PreferenceManager.getDefaultSharedPreferences(appContext)

    private val _waterIntake = MutableStateFlow(prefs.getInt("water_intake_today", 0))
    val waterIntake: StateFlow<Int> = _waterIntake.asStateFlow()

    private val _dailyGoal = MutableStateFlow(prefs.getInt("water_daily_goal", 3000))
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()

    private val _cupSize = MutableStateFlow(prefs.getInt("water_cup_size", 250))
    val cupSize: StateFlow<Int> = _cupSize.asStateFlow()

    private val _waterHistory = MutableStateFlow<List<WaterEntry>>(emptyList())
    val waterHistory: StateFlow<List<WaterEntry>> = _waterHistory.asStateFlow()

    private val _reminderEnabled = MutableStateFlow(prefs.getBoolean("water_reminder_enabled", false))
    val reminderEnabled: StateFlow<Boolean> = _reminderEnabled.asStateFlow()

    private val _reminderInterval = MutableStateFlow(prefs.getInt("water_reminder_interval", 60))
    val reminderInterval: StateFlow<Int> = _reminderInterval.asStateFlow()

    fun addWater(amount: Int) {
        viewModelScope.launch {
            val newTotal = _waterIntake.value + amount
            _waterIntake.value = newTotal
            _waterHistory.value = listOf(WaterEntry(amount)) + _waterHistory.value
            prefs.edit().putInt("water_intake_today", newTotal).apply()
        }
    }

    fun setDailyGoal(goal: Int) {
        viewModelScope.launch {
            _dailyGoal.value = goal
            prefs.edit().putInt("water_daily_goal", goal).apply()
        }
    }

    fun setCupSize(size: Int) {
        viewModelScope.launch {
            _cupSize.value = size
            prefs.edit().putInt("water_cup_size", size).apply()
        }
    }

    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _reminderEnabled.value = enabled
            prefs.edit().putBoolean("water_reminder_enabled", enabled).apply()
            if (enabled) scheduleNextReminder() else cancelReminder()
        }
    }

    fun setReminderInterval(minutes: Int) {
        viewModelScope.launch {
            _reminderInterval.value = minutes
            prefs.edit().putInt("water_reminder_interval", minutes).apply()
            if (_reminderEnabled.value) scheduleNextReminder()
        }
    }

    fun resetDailyIntake() {
        viewModelScope.launch {
            _waterIntake.value = 0
            _waterHistory.value = emptyList()
            prefs.edit().putInt("water_intake_today", 0).apply()
        }
    }

    private fun reminderPendingIntent(): PendingIntent {
        val intent = Intent(appContext, WaterReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            appContext,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun scheduleNextReminder() {
        val minutes = _reminderInterval.value.coerceAtLeast(15)
        val triggerAt = System.currentTimeMillis() + minutes * 60_000L

        val am = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = reminderPendingIntent()

        am.cancel(pi)
        am.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pi
        )
    }

    fun cancelReminder() {
        val am = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(reminderPendingIntent())
    }
}