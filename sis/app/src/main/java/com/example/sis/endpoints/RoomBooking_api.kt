package com.example.sis.endpoints

import com.example.sis.datamodels.RoomBooking
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomBooking_api {
    @POST("/new_roomBooking/")
    suspend fun newRoomBooking(
        @Body roomBooking: RoomBooking
    ): RoomBooking

    @GET("/all_roomBooking")
    suspend fun allRoomBooking(): RoomBooking

    @GET("/roomBooking/{id}")
    suspend fun  getRoomBookingId(
        @Path("id") id: Int
    )

    @DELETE("/delete_roomBooking/{id}")
    suspend fun deleteRoomBookingId(
        @Path("id") id: Int
    )
}