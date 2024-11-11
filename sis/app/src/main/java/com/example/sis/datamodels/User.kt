package com.example.sis.datamodels

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val birthday: String,
    val phone: String,
    val role_id: Int,
    val token: String
)