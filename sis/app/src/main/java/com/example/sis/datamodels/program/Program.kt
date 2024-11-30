package com.example.sis.datamodels.program

data class Program(
    val name: String,
    val description: String
)

data class ProgramById(
    val id: Int,
    val name: String,
    val description: String
)