package ch.hearc.medicalcheckwear.services

import ch.hearc.medicalcheckwear.models.Measure
import ch.hearc.medicalcheckwear.models.Notification
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * interface which allow to create a request to manage the messures in our database
 */
interface  MeasureService {

    /*
    * create a new measure when it's call, the measure need to be attached to the body
    */
    @POST("measures")
    fun create(@Body measure: Measure): Call<Measure>
}