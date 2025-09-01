package com.aarti.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SleepEntry(
    val startTime: Long,
    val endTime: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTrackerScreen(onBack: () -> Unit) {
    var running by remember { mutableStateOf(false) }
    var currentStart by remember { mutableStateOf(0L) }
    var entries by remember { mutableStateOf(listOf<SleepEntry>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sleep Tracker") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Simple session tracker", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    if (!running) {
                        running = true
                        currentStart = System.currentTimeMillis()
                    }
                }, enabled = !running) { Text("Start") }

                Button(onClick = {
                    if (running) {
                        running = false
                        val end = System.currentTimeMillis()
                        entries = listOf(SleepEntry(currentStart, end)) + entries
                    }
                }, enabled = running) { Text("Stop") }
            }

            Text("History", style = MaterialTheme.typography.titleMedium)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(entries) { e ->
                    SleepRow(e)
                }
            }
        }
    }
}

@Composable
private fun SleepRow(e: SleepEntry) {
    val sdf = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    val durationMin = ((e.endTime - e.startTime) / 60000).coerceAtLeast(0)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Start: ${sdf.format(Date(e.startTime))}")
            Text("End:   ${sdf.format(Date(e.endTime))}")
        }
        Text("${durationMin}m")
    }
}