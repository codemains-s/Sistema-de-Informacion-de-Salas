package com.example.sis.datamodels.room

data class Room(
    val id: Int,
    val name: String,
    val description: String,
    val capacity: String,
    val status: String,
    val image: String
)