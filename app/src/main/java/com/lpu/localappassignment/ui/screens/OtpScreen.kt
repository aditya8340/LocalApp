package com.lpu.localappassignment.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lpu.localappassignment.viewmodel.OtpError

@Composable
fun OtpScreen(
    email: String,
    attemptsLeft: Int,
    expirySeconds: Int,
    error: OtpError?,
    debugOtp: String?,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit
) {
    val otpState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Verify OTP",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "OTP sent to $email",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        // DEBUG OTP
        if (debugOtp != null) {
            Text(
                text = "DEBUG OTP: $debugOtp",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = otpState.value,
            onValueChange = {
                if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                    otpState.value = it
                }
            },
            label = { Text("6-digit OTP") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Expires in ${expirySeconds}s • Attempts left: $attemptsLeft",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (error != null) {
            Text(
                text = when (error) {
                    is OtpError.Expired -> "OTP expired"
                    is OtpError.Incorrect -> "Incorrect OTP"
                    is OtpError.AttemptsExceeded -> "Attempts exceeded"
                },
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = { onVerifyOtp(otpState.value) },
            enabled = otpState.value.length == 6 && attemptsLeft > 0,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Verify OTP")
        }

        TextButton(
            onClick = {
                otpState.value = ""
                onResendOtp()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Resend OTP")
        }
    }

}
