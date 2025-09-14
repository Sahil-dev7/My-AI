package com.aarti.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aarti.ai.ui.theme.AartiTheme

class MoneyTrackerScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AartiTheme {
                MoneyTrackerScreen(onBack = { finish() })
            }
        }
    }
}
