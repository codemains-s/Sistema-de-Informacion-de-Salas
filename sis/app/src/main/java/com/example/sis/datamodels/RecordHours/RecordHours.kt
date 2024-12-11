package com.example.sis.datamodels.RecordHours

data class RecordHours(
    val room_booking_id: Int,
    val user_id: Int,
    val hour_completed: Int,
    val date_register: String,
    val status: String,
    val signature: String,
    val observations: String
)
