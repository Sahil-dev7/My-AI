package com.aarti.ai   // <-- Yeh top pe hona hi chahiye

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

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Android 12+ system splash ko install karo
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // System splash ko immediately hata dena (hum apna Compose splash dikhayenge)
        splashScreen.setKeepOnScreenCondition { false }

        setContent {
            AartiTheme {
                SplashScreenContent(
                    onTimeout = {
                        // 🔹 SharedPreferences se check karo
                        val prefs = getSharedPreferences(Prefs.NAME, MODE_PRIVATE)
                        val isLoggedIn = prefs.getBoolean(Prefs.KEY_LOGGED_IN, false)

                        // 🔹 Destination decide karo
                        val destination = if (isLoggedIn) {
                            Intent(this, MainActivity::class.java)
                        } else {
                            Intent(this, LoginActivity::class.java)
                        }

                        startActivity(destination)
                        finish() // back button par wapas splash na aaye
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreenContent(onTimeout: () -> Unit) {
    // Composable ke andar launch hota hai → delay ke baad navigation
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds splash dikhana
        onTimeout()
    }

    // Centered column me logo + app name
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.aarti_logo_ultimate_merge),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier.size(180.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold
        )
    }
}