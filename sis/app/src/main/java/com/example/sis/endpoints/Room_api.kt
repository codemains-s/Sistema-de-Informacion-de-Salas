package com.example.sis.endpoints

import com.example.sis.datamodels.Room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Room_api {
    @POST("/new_room/")
    suspend fun newRoom(
        @Body room: Room
    ): Room

    @GET("/all_room")
    suspend fun allRoom(): Room

    @GET("/room/{id}")
    suspend fun getRoomId(
        @Path("id") id: Int
    )
}