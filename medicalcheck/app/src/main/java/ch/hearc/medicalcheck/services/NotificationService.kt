package ch.hearc.medicalcheck.services

import ch.hearc.medicalcheck.models.Notification
import ch.hearc.medicalcheck.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

interface  NotificationService {
    @GET("notifications")
    fun getAll(): Call<List<Notification>>

    @GET("notifications/{id}")
    fun get(@Path("id") id : Int): Call<Notification>

    @GET("notifications/user/{iduser}")
    fun getAllByIdUser(@Path("iduser") iduser : Int): Call<List<Notification>>

    @POST("notifications")
    fun create(@Body notification: Notification): Call<Notification>

    @DELETE("notifications/{id}")
    fun delete(@Path("id") id: Int): Call<ResponseBody>

    @PUT("notifications/{id}")
    fun update(@Path("id") id : Int, @Body notification: Notification): Call<Notification>
}