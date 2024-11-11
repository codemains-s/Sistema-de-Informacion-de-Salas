package com.example.sis.datamodels

data class RoomSchedule(
    val id: Int,
    val room_id: Int,
    val schedule_id: Int,
    val date: String,
    val hour: String
)