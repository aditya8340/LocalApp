package com.lpu.localappassignment.data

sealed class OtpValidationResult {
    object Success : OtpValidationResult()
    object Expired : OtpValidationResult()
    object AttemptsExceeded : OtpValidationResult()
    object Invalid : OtpValidationResult()
    data class Incorrect(val attemptsLeft: Int) : OtpValidationResult()
}
