package com.example.sis.datamodels.user

import java.util.Date

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val birthdate: Date,
    val phone: String,
    val role_id: Int,
    val token: String,
    val program_id: String
)