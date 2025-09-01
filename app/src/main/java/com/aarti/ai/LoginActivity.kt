package com.aarti.ai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aarti.ai.ui.theme.AartiTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AartiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        onLoginSuccess = { email, password ->
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                val prefs = getSharedPreferences(Prefs.NAME, MODE_PRIVATE)
                                prefs.edit()
                                    .putBoolean(Prefs.KEY_LOGGED_IN, true)
                                    .putString(Prefs.KEY_EMAIL, email)
                                    .putString("user_password", password) // optional
                                    .apply()

                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onNavigateToRegister = {
                            startActivity(Intent(this, RegistrationActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onLoginSuccess(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Login", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text(
                text = "Don't have an account? Register here.",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AartiTheme {
        LoginScreen(onLoginSuccess = { _, _ -> }, onNavigateToRegister = {})
    }
}