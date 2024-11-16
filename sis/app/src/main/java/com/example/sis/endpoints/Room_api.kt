package com.example.sis.endpoints

import com.example.sis.datamodels.room.Room
import com.example.sis.datamodels.room.RoomCreate
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

<<<<<<< HEAD
    @GET("/all_rooms")
    suspend fun allRoom(): List<Room> // Ahora devuelve una lista

    @GET("/room_by_id")
    suspend fun getRoomId(
        @Query("id") id: Int // Cambiado para usar @Query
    ): Room // Asegúrate de que devuelva un objeto Room

=======
    @GET("/all_rooms/")
    suspend fun get_rooms(): List<RoomCreate>

    @GET("/room_by_name/")
    suspend fun get_room_by_name(name: String):Room

>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
}