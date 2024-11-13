package com.example.sis.endpoints

import com.example.sis.datamodels.UserRoom
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserRoom_api {
    @POST("/new_userRoom/")
    suspend fun newUserRoom(
        @Body userRoom: UserRoom
    ): UserRoom

    @GET("/all_userRoom")
    suspend fun allUserRoom(): UserRoom

    @GET("/userRoom/{id}")
    suspend fun  getUserRoomId(
        @Path("id") id: Int
    )
}