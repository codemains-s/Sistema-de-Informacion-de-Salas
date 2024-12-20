package com.example.sis.datamodels

data class RoomBooking (
    val user_id: Int,
    val room_id: Int,
    val booking_date: String,
    val start_time: String,
    val end_time: String,
    val status: String,
)

data class RoomBookingId(
    val id: Int,
    val user_id: Int,
    val room_id: Int,
    val booking_date: String,
    val start_time: String,
    val end_time: String,
    val status: String,
)

data class DeleteRoomBooking(
    val message: String
)
