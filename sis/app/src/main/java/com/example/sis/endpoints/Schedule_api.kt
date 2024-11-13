package com.example.sis.endpoints

import com.example.sis.datamodels.Schedule
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Schedule_api{
    @POST("/new_schedule/")
    suspend fun newSchedule(
        @Body schedule: Schedule
    ): Schedule

    @GET("/all_schedule")
    suspend fun allSchedule(): Schedule

    @GET("/schedule/{id}")
    suspend fun  getSchefuleId(
        @Path("id") id: Int
    )
}