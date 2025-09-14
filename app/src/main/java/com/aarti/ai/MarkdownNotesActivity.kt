package com.aarti.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aarti.ai.ui.theme.AartiTheme

class MarkdownNotesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AartiTheme {
                MarkdownNotesScreen(onBack = { finish() })
            }
        }
    }
}
