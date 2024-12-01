package com.example.sis.endpoints

import com.example.sis.datamodels.RoomSchedule
import com.example.sis.datamodels.RoomScheduleId
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomSchedule_api {
    @POST("/new_roomSchedule/")
    suspend fun newRoomSchedule(
        @Body roomSchedule: RoomSchedule,
        @Header("Authorization") authHeader: String
    ): RoomSchedule

    @GET("/all_roomSchedule")
    suspend fun allRoomSchedule(

    ): Response<List<RoomScheduleId>>

    @GET("/roomSchedule/{id}")
    suspend fun  getRoomScheduleId(
        @Path("id") id: Int
    )

    @GET("/roomScheduleByRoomId/{id}")
    suspend fun getRoomScheduleByRoomId(
        @Path("id") id: Int,

    ): Response<List<RoomScheduleId>>

}