package com.aarti.ai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aarti.ai.ui.theme.AartiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // prefs read karna
        val prefs = getSharedPreferences(Prefs.NAME, MODE_PRIVATE)
        val savedName = prefs.getString(Prefs.KEY_NAME, null)
        val savedEmail = prefs.getString(Prefs.KEY_EMAIL, null)

        setContent {
            AartiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        userName = savedName ?: savedEmail ?: "User",
                        onOpenWater = {
                            startActivity(Intent(this, WaterTrackerScreenActivity::class.java))
                        },
                        onOpenSleep = {
                            startActivity(Intent(this, SleepTrackerScreenActivity::class.java))
                        },
                        onOpenMoney = {
                            startActivity(Intent(this, MoneyTrackerScreenActivity::class.java))
                        },
                        onOpenMarkdownNotes = {
                            startActivity(Intent(this, MarkdownNotesActivity::class.java))
                        },
                        onLogout = {
                            prefs.edit().putBoolean(Prefs.KEY_LOGGED_IN, false).apply()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    userName: String,
    onOpenWater: () -> Unit,
    onOpenSleep: () -> Unit,
    onOpenMoney: () -> Unit,
    onOpenMarkdownNotes: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome, $userName 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onOpenWater, modifier = Modifier.height(56.dp)) {
            Text("Open Water Tracker")
        }

        Button(onClick = onOpenSleep, modifier = Modifier.height(56.dp)) {
            Text("Open Sleep Tracker")
        }

        Button(onClick = onOpenMoney, modifier = Modifier.height(56.dp)) {
            Text("Open Money Tracker")
        }

        Button(onClick = onOpenMarkdownNotes, modifier = Modifier.height(56.dp)) {
            Text("Open Markdown Notes")
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.height(50.dp)
        ) {
            Text("Logout")
        }
    }
}

// TODO: Implement offline storage for notes, chats, tasks
// TODO: Add modular architecture for custom add-ons
// TODO: Enhance lovable personality features

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AartiTheme {
        MainScreen(
            userName = "Sahil",
            onOpenWater = {},
            onOpenSleep = {},
            onOpenMoney = {},
            onOpenMarkdownNotes = {},
            onLogout = {}
        )
    }
}