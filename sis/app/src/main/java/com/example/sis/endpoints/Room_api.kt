package com.example.sis.endpoints

import com.example.sis.datamodels.room.Room
import com.example.sis.datamodels.room.RoomCreate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Room_api {
    @POST("/new_room/")
    suspend fun newRoom(
        @Body room: Room
    ): Room

    @GET("/all_rooms/")
    suspend fun get_rooms(): List<RoomCreate>

    @GET("/room_by_name/")
    suspend fun get_room_by_name(name: String):Room

}