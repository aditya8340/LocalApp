package com.lpu.localappassignment.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SessionScreen(
    startTimeMillis: Long,
    elapsedSeconds: Long,
    onLogout: () -> Unit
) {
    val startTimeFormatted = SimpleDateFormat(
        "HH:mm:ss",
        Locale.getDefault()
    ).format(Date(startTimeMillis))

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val durationFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Session Active",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Started at $startTimeFormatted",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = durationFormatted,
            style = MaterialTheme.typography.displaySmall
        )

        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text("Logout")
        }
    }

}
