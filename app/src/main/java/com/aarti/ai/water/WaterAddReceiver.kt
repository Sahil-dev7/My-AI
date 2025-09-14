package com.aarti.ai.water

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager

class WaterAddReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.aarti.ai.ACTION_ADD_WATER") {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val cup = prefs.getInt("water_cup_size", 250)
            val total = prefs.getInt("water_intake_today", 0) + cup
            prefs.edit().putInt("water_intake_today", total).apply()
        }
    }
}