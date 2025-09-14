package com.aarti.ai.water

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTrackerScreen(
    waterViewModel: WaterViewModel = viewModel(),
    onBack: () -> Unit
) {
    val waterIntake by waterViewModel.waterIntake.collectAsState()
    val dailyGoal by waterViewModel.dailyGoal.collectAsState()
    val cupSize by waterViewModel.cupSize.collectAsState()
    val history by waterViewModel.waterHistory.collectAsState()
    val reminderEnabled by waterViewModel.reminderEnabled.collectAsState()
    val reminderInterval by waterViewModel.reminderInterval.collectAsState()

    val context = LocalContext.current
    val progress = (waterIntake.toFloat() / dailyGoal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Water Tracker") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { waterViewModel.addWater(cupSize) }) {
                Icon(Icons.Default.Add, contentDescription = "Add water")
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Today: ${waterIntake}ml / ${dailyGoal}ml",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())

            // Cup size & Goal inputs
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = cupSize.toString(),
                    onValueChange = { it.toIntOrNull()?.let(waterViewModel::setCupSize) },
                    label = { Text("Cup ml") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = dailyGoal.toString(),
                    onValueChange = { it.toIntOrNull()?.let(waterViewModel::setDailyGoal) },
                    label = { Text("Daily goal ml") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Reminder toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("Reminder", style = MaterialTheme.typography.titleSmall)
                    Text("Interval: ${reminderInterval} min", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = reminderEnabled, onCheckedChange = { waterViewModel.setReminderEnabled(it) })
            }

            Slider(
                value = reminderInterval.toFloat(),
                onValueChange = { waterViewModel.setReminderInterval(it.toInt()) },
                valueRange = 15f..180f
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { waterViewModel.addWater(cupSize) }) {
                    Text("Add ${cupSize}ml")
                }
                OutlinedButton(onClick = { waterViewModel.resetDailyIntake() }) {
                    Text("Reset today")
                }
            }

            // Allow exact alarm settings shortcut
            OutlinedButton(onClick = {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
            }) {
                Text("Allow exact alarm (if required)")
            }

            Text("History", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(history) { entry ->
                    HistoryRow(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(entry: WaterEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("+${entry.amountMl} ml", style = MaterialTheme.typography.titleSmall)
            Text(
                SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(entry.timestamp)),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}