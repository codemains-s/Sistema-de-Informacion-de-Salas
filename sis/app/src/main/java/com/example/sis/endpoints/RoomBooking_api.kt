package com.example.sis.endpoints

import com.example.sis.datamodels.RoomBooking
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RoomBooking_api {
    @POST("/new_roomBooking/")
    suspend fun newRoomBooking(
        @Body roomBooking: RoomBooking
    ): RoomBooking


    @GET("/all_roomBookings/")
    suspend fun allRoomBooking(
        @Header("Authorization") authHeader: String
    ): Response<List<RoomBooking>>



    @GET("/roomBooking/{id}")
    suspend fun  getRoomBookingId(
        @Path("id") id: Int
    )

    @DELETE("/delete_roomBooking/{id}")
    suspend fun deleteRoomBookingId(
        @Path("id") id: Int
    )
}

