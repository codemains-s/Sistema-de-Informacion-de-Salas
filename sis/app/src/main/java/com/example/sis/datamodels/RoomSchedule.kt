package com.example.sis.datamodels

data class RoomSchedule(
    val room_id: Int,
    val hour_start: String,
    val hour_end: String,
    val day_of_week: String,
    val status: String
)

data class RoomScheduleId(
    val id: Int,
    val room_id: Int,
    val hour_start: String,
    val hour_end: String,
    val day_of_week: String,
    val status: String
)