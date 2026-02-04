package com.lpu.localappassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lpu.localappassignment.data.OtpManager
import com.lpu.localappassignment.data.OtpValidationResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lpu.localappassignment.analytics.AnalyticsLogger

class AuthViewModel : ViewModel() {

    private val otpManager = OtpManager()

    var debugOtp: String? = null
        private set

    // Exposed UI state
//    var uiState: AuthUiState = AuthUiState.EmailEntry
//        private set
    var uiState by mutableStateOf<AuthUiState>(AuthUiState.EmailEntry)
        private set

    private var otpCountdownJob: Job? = null
    private var sessionTimerJob: Job? = null

    /** User clicks "Send OTP" */
    fun sendOtp(email: String) {
        val otpData = otpManager.generateOtp(email)
        AnalyticsLogger.logOtpGenerated(email)


        // 🔧 DEBUG ONLY
        debugOtp = otpData.otp

        uiState = AuthUiState.OtpEntry(
            email = email,
            attemptsLeft = otpData.attemptsLeft,
            otpExpirySecondsLeft = 60,
            error = null
        )
        startOtpCountdown(otpData.expiresAtMillis)
    }

    /** User submits OTP */
    fun verifyOtp(email: String, enteredOtp: String) {
        when (val result = otpManager.validateOtp(email, enteredOtp)) {

            is OtpValidationResult.Success -> {
                AnalyticsLogger.logOtpSuccess(email)
                otpCountdownJob?.cancel()
                startSession()
            }

            is OtpValidationResult.Expired -> {
                AnalyticsLogger.logOtpFailure(email, "OTP Expired")
                uiState = (uiState as AuthUiState.OtpEntry).copy(
                    error = OtpError.Expired
                )
            }

            is OtpValidationResult.AttemptsExceeded -> {
                AnalyticsLogger.logOtpFailure(email, "Attempts Exceeded")
                uiState = (uiState as AuthUiState.OtpEntry).copy(
                    error = OtpError.AttemptsExceeded,
                    attemptsLeft = 0
                )
            }

            is OtpValidationResult.Incorrect -> {
                AnalyticsLogger.logOtpFailure(email, "Incorrect OTP")
                uiState = (uiState as AuthUiState.OtpEntry).copy(
                    error = OtpError.Incorrect,
                    attemptsLeft = result.attemptsLeft
                )
            }

            is OtpValidationResult.Invalid -> {
                uiState = (uiState as AuthUiState.OtpEntry).copy(
                    error = OtpError.Expired
                )
            }
        }
    }

    /** Starts OTP countdown */
    private fun startOtpCountdown(expiryTimeMillis: Long) {
        otpCountdownJob?.cancel()

        otpCountdownJob = viewModelScope.launch {
            while (true) {
                val remainingSeconds =
                    ((expiryTimeMillis - System.currentTimeMillis()) / 1000).toInt()

                if (remainingSeconds <= 0) break

                uiState = (uiState as AuthUiState.OtpEntry).copy(
                    otpExpirySecondsLeft = remainingSeconds
                )

                delay(1000)
            }
        }
    }

    /** Starts session timer after successful login */
    private fun startSession() {
        val startTime = System.currentTimeMillis()

        uiState = AuthUiState.Session(
            sessionStartTimeMillis = startTime,
            elapsedSeconds = 0
        )

        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (true) {
                val elapsed =
                    ((System.currentTimeMillis() - startTime) / 1000)

                uiState = AuthUiState.Session(
                    sessionStartTimeMillis = startTime,
                    elapsedSeconds = elapsed
                )

                delay(1000)
            }
        }
    }

    /** User logs out */
    fun logout() {
        sessionTimerJob?.cancel()
        AnalyticsLogger.logLogout()
        uiState = AuthUiState.EmailEntry
    }
}
