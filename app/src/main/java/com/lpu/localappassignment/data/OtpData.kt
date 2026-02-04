package com.lpu.localappassignment.data

data class OtpData(
    val otp: String,
    val createdAtMillis: Long,
    val expiresAtMillis: Long,
    val attemptsLeft: Int
)
