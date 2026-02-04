package com.lpu.localappassignment.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmailScreen(
    onSendOtp: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Sign in using your email to continue",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )

        Button(
            onClick = { onSendOtp(email.trim()) },
            enabled = email.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Send OTP")
        }
    }
}
