package com.example.sis.endpoints

import com.example.sis.datamodels.Room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Room_api {
    @POST("/new_room/")
    suspend fun newRoom(
        @Body room: Room
    ): Room

    @GET("/all_rooms")
    suspend fun allRoom(): List<Room> // Ahora devuelve una lista

    @GET("/room_by_id")
    suspend fun getRoomId(
        @Query("id") id: Int // Cambiado para usar @Query
    ): Room // Asegúrate de que devuelva un objeto Room

}