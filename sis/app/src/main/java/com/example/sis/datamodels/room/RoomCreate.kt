package com.example.sis.datamodels.room

data class RoomCreate (
    val name: String,
    val address: String,
    val description: String,
    val capacity: Int,
    val status: String,
    val image: String
)