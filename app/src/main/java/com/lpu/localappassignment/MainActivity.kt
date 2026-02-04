package com.lpu.localappassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lpu.localappassignment.ui.theme.LocalAppAssignmentTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lpu.localappassignment.viewmodel.AuthUiState
import com.lpu.localappassignment.viewmodel.AuthViewModel
import com.lpu.localappassignment.ui.screens.EmailScreen
import com.lpu.localappassignment.ui.screens.OtpScreen
import com.lpu.localappassignment.ui.screens.SessionScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalAppAssignmentTheme {

                val authViewModel: AuthViewModel = viewModel()
                val uiState = authViewModel.uiState

                when (uiState) {
                    is AuthUiState.EmailEntry -> {
                        EmailScreen(
                            onSendOtp = { email ->
                                authViewModel.sendOtp(email)
                            }
                        )
                    }

                    is AuthUiState.OtpEntry -> {
                        val state = uiState as AuthUiState.OtpEntry
                        OtpScreen(
                            email = state.email,
                            attemptsLeft = state.attemptsLeft,
                            expirySeconds = state.otpExpirySecondsLeft,
                            error = state.error,
                            debugOtp = authViewModel.debugOtp,// this here will pass the test otp to otpscreen
                            onVerifyOtp = { otp ->
                                authViewModel.verifyOtp(state.email, otp)
                            },
                            onResendOtp = {
                                authViewModel.sendOtp(state.email)
                            }
                        )
                    }

                    is AuthUiState.Session -> {
                        val state = uiState as AuthUiState.Session
                        SessionScreen(
                            startTimeMillis = state.sessionStartTimeMillis,
                            elapsedSeconds = state.elapsedSeconds,
                            onLogout = {
                                authViewModel.logout()
                            }
                        )
                    }
                }
            }
        }

    }
}