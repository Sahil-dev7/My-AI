package com.aarti.ai

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager

class WaterReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Build progress text
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val total = prefs.getInt("water_intake_today", 0)
        val goal = prefs.getInt("water_daily_goal", 3000)
        val progressText = "Drank ${total}ml / ${goal}ml"

        // Channel
        val channelId = "water_reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelId,
                "Water Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        // Tap -> open app
        val appIntent = Intent(context, MainActivity::class.java)
        val appPendingIntent = PendingIntent.getActivity(
            context, 0, appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action -> add one cup
        val addIntent = Intent("com.aarti.ai.ACTION_ADD_WATER")
            .setClass(context, WaterAddReceiver::class.java)
        val addPendingIntent = PendingIntent.getBroadcast(
            context, 2002, addIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time to drink water")
            .setContentText(progressText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(progressText))
            .setAutoCancel(true)
            .setContentIntent(appPendingIntent)
            .addAction(0, "Add 1 cup", addPendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(12345, builder.build())
        }

        // Schedule next reminder
        val minutes = prefs.getInt("water_reminder_interval", 60).coerceAtLeast(15)
        val triggerAt = System.currentTimeMillis() + minutes * 60_000L

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextIntent = Intent(context, WaterReminderReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context, 1001, nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        am.cancel(pi)
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
    }
}