package com.lpu.localappassignment.viewmodel

sealed class AuthUiState {

    /**
     * App start state
     * User enters email and clicks "Send OTP"
     */
    object EmailEntry : AuthUiState()

    /**
     * OTP screen
     * Shows timer, attempts left, and possible errors
     */
    data class OtpEntry(
        val email: String,
        val attemptsLeft: Int,
        val otpExpirySecondsLeft: Int,
        val error: OtpError? = null
    ) : AuthUiState()

    /**
     * Logged-in session screen
     * Shows session start time and running duration
     */
    data class Session(
        val sessionStartTimeMillis: Long,
        val elapsedSeconds: Long
    ) : AuthUiState()
}

/**
 * Possible OTP errors shown to the user
 */
sealed class OtpError {
    object Expired : OtpError()
    object Incorrect : OtpError()
    object AttemptsExceeded : OtpError()
}
