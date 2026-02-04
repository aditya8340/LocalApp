package com.lpu.localappassignment.data

import kotlin.random.Random

class OtpManager {

    private val otpStore: MutableMap<String, OtpData> = mutableMapOf()

    private val OTP_LENGTH = 6
    private val OTP_EXPIRY_MILLIS = 60_000L
    private val MAX_ATTEMPTS = 3

    fun generateOtp(email: String): OtpData {
        val otp = (1..OTP_LENGTH)
            .map { Random.nextInt(0, 10) }
            .joinToString("")

        val now = System.currentTimeMillis()
        val otpData = OtpData(
            otp = otp,
            createdAtMillis = now,
            expiresAtMillis = now + OTP_EXPIRY_MILLIS,
            attemptsLeft = MAX_ATTEMPTS
        )

        otpStore[email] = otpData
        return otpData
    }

    fun validateOtp(email: String, enteredOtp: String): OtpValidationResult {
        val otpData = otpStore[email] ?: return OtpValidationResult.Invalid

        val now = System.currentTimeMillis()

        if (now > otpData.expiresAtMillis) {
            otpStore.remove(email)
            return OtpValidationResult.Expired
        }

        if (otpData.attemptsLeft <= 0) {
            otpStore.remove(email)
            return OtpValidationResult.AttemptsExceeded
        }

        return if (enteredOtp == otpData.otp) {
            otpStore.remove(email)
            OtpValidationResult.Success
        } else {
            otpStore[email] = otpData.copy(
                attemptsLeft = otpData.attemptsLeft - 1
            )
            OtpValidationResult.Incorrect(otpData.attemptsLeft - 1)
        }
    }
}
