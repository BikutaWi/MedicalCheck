package ch.hearc.medicalcheck.services

import ch.hearc.medicalcheck.models.Planning
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

interface  PlanningService {
    @GET("plannings")
    fun getAll(): Call<List<Planning>>

    @GET("plannings/{id}")
    fun get(@Path("id") id : Int): Call<Planning>

    @GET("plannings/today/{iduser}/{day}")
    fun getAllTodayByIdUser(@Path("iduser") iduser : Int, @Path("day") day : String) : Call<List<Planning>>

    @GET("plannings/medicine/{idmedicine}/")
    fun getAllByIdMedicine(@Path("idmedicine") idmedicine : Int) : Call<List<Planning>>

    @POST("plannings")
    fun create(@Body planning: Planning): Call<Planning>

    @DELETE("plannings/{id}")
    fun delete(@Path("id") id: Int): Call<ResponseBody>

    @PUT("plannings/{id}")
    fun update(@Path("id") id : Int, @Body planning: Planning): Call<Planning>
}