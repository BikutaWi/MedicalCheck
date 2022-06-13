package ch.hearc.medicalcheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.services.MeasureService
import ch.hearc.medicalcheck.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class GraphViewModel : ViewModel() {
    private val measures = MutableLiveData<Map<Int, Double>>()
    fun getMeasures() : LiveData<Map<Int, Double>> = measures

    /***
     * try to log in user
     */
    fun loadMeasures(date: String) {
        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(MeasureService::class.java)

        // get measure form Rest service
        val measureRequest = service?.getMapAverage(Utils.userId, date)

        // enqueue query response
        measureRequest?.enqueue(object : Callback<Map<Int, Double>> {
            override fun onResponse(call: Call<Map<Int, Double>>, response: Response<Map<Int, Double>>) {

                // take map from repsonse
                val map = response.body()

                // if measures exist
                if (map != null) {

                    val sortedMap = TreeMap(map)
                    measures.value = sortedMap
                }
            }

            override fun onFailure(call: Call<Map<Int, Double>>, t: Throwable) {
                error("error on getting measure data")
            }
        })
    }
}