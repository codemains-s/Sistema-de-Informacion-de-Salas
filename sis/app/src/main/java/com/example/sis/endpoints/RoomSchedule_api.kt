package com.example.sis.endpoints

import com.example.sis.datamodels.RoomSchedule
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomSchedule_api {
    @POST("/new_roomSchedule/")
    suspend fun newRoomSchedule(
        @Body roomSchedule: RoomSchedule
    ): RoomSchedule

    @GET("/all_roomSchedule")
    suspend fun allRoomSchedule(): RoomSchedule

    @GET("/roomSchedule/{id}")
    suspend fun  getRoomScheduleId(
        @Path("id") id: Int
    )

}