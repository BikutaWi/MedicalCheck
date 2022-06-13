package ch.hearc.medicalcheck.services

import ch.hearc.medicalcheck.models.Measure
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

interface  MeasureService {
    @GET("measures")
    fun getAll(): Call<List<Measure>>

    @GET("measures/{id}")
    fun get(@Path("id") id : Int): Call<Measure>

    @GET("measures/user/{iduser}")
    fun getAllByIdUser(@Path("iduser") iduser: Int): Call<List<Measure>>

    @GET("measures/user/last/{iduser}")
    fun getLastByIdUser(@Path("iduser") iduser: Int): Call<Measure>

    @GET("measures/user/average/{iduser}/{date}")
    fun getMapAverage(@Path("iduser") iduser: Int?, @Path("date") date: String?): Call<Map<Int, Double>>

    @POST("measures")
    fun create(@Body measure: Measure): Call<Measure>

    @DELETE("measures/{id}")
    fun delete(@Path("id") id: Int): Call<ResponseBody>

    @PUT("measures/{id}")
    fun update(@Path("id") id : Int, @Body measure: Measure): Call<Measure>
}