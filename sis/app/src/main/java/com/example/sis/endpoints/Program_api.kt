package com.example.sis.endpoints

import com.example.sis.datamodels.program.Program
import com.example.sis.datamodels.program.ProgramById
import com.example.sis.datamodels.program.ProgramId
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Program_api {
    @POST("/new_program/")
    suspend fun newProgram(
        @Body program: Program,
        @Header("Authorization") authHeader: String
    ): Program

    @GET("/all_programs/")
    suspend fun allPrograms(
    ): List<ProgramById>

    @GET("/program/{id}")
    suspend fun programForId(
        @Query("id") id: Int,
        @Header("Authorization") authHeader: String
    ): Program

    @GET("/program_id/{name}")
    suspend fun programId(
        @Query("name") name: String,
        @Header("Authorization") authHeader: String
    ): ProgramId
}