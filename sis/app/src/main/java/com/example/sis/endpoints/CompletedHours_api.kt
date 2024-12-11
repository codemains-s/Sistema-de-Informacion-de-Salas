package com.example.sis.endpoints

import com.example.sis.datamodels.RecordHours.RecordHours
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CompletedHours_api {
    @POST("/new_completed_hour/")
    suspend fun registrarHoras(
        @Body recordHours: RecordHours,
        @Header("Authorization") token: String
    ): RecordHours
}