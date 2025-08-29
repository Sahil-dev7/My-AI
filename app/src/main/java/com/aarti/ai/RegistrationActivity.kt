package com.aarti.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aarti.ai.ui.theme.AartiTheme

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AartiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistrationScreen()
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen() {
    Text(text = "Registration Screen - Coming Soon!", color = MaterialTheme.colorScheme.onBackground)
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    AartiTheme {
        RegistrationScreen()
    }
}
