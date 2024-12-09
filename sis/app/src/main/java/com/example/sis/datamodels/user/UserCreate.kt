package com.example.sis.datamodels.user

data class UserCreate(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val program_id:Int
)