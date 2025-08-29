package com.aarti.ai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aarti.ai.ui.theme.AartiTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen") // Suppress warning for using custom splash screen
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen on-screen until content is loaded.
        // In a real app, you might fetch data or check login status here.
        splashScreen.setKeepOnScreenCondition { false } // Set to true if you have data loading

        setContent {
            AartiTheme {
                SplashScreenContent() // Our Compose Splash Screen UI
            }
        }

        // Simulate a delay and then navigate
        LaunchedEffect(Unit) {
            delay(3000) // 3 seconds delay
            // Check login status (Session Management - will implement later)
            val isLoggedIn = false // Placeholder for now

            val destination = if (isLoggedIn) {
                Intent(this@SplashScreenActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashScreenActivity, LoginActivity::class.java)
            }
            startActivity(destination)
            finish()
        }
    }
}

@Composable
fun SplashScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.aarti_logo_ultimate_merge), // Your logo drawable
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(180.dp) // Adjust size as needed
        )
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
