package com.lpu.localappassignment.analytics

import timber.log.Timber

object AnalyticsLogger {

    fun logOtpGenerated(email: String) {
        Timber.d("OTP generated for email: $email")
    }

    fun logOtpSuccess(email: String) {
        Timber.d("OTP validation SUCCESS for email: $email")
    }

    fun logOtpFailure(email: String, reason: String) {
        Timber.d("OTP validation FAILURE for email: $email, reason: $reason")
    }

    fun logLogout() {
        Timber.d("User logged out")
    }
}
