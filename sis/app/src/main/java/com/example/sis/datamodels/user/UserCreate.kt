package com.example.sis.datamodels.user

import java.util.Date

data class UserCreate(
    val name: String,
    val email: String,
    val birthday: String,
    val phone: String,
    val password: String
)