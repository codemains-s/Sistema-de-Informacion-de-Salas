package com.example.sis.datamodels.user

data class UserCreate(
    val name: String,
    val email: String,
    val birthdate: String,
    val phone: String,
    val password: String
)