package ch.hearc.medicalcheck.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.models.Measure
import ch.hearc.medicalcheck.models.Planning
import ch.hearc.medicalcheck.models.Treatment
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.MeasureService
import ch.hearc.medicalcheck.services.PlanningService
import ch.hearc.medicalcheck.services.TreatmentService
import ch.hearc.medicalcheck.services.UsersService
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.viewmodels.UpdateProfilViewModelStateFailure
import ch.hearc.medicalcheck.viewmodels.UpdateProfilViewModelStateSuccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class HomeViewModel : ViewModel() {

    //live data
    private val firstname = MutableLiveData<String>()
    private val measure = MutableLiveData<Measure>()
    private val treatment = MutableLiveData<List<Treatment>>()
    fun getFirstname() : LiveData<String> = firstname
    fun getMeasure() : LiveData<Measure> = measure
    fun getTreatment() : LiveData<List<Treatment>> = treatment

    /***
     * Load user and return id and firstname to show on fragment view
     */
    fun loadUser() {

        // get connected user's id
        val userid: Int = Utils.userId

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(UsersService::class.java)

        // get query from Rest service
        val userRequest = service?.get(userid)

        // enqueue request response
        userRequest?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                // take user model from response
                val user = response.body()

                // if user is not null (if user exist)
                if (user != null)
                {
                    // update data
                    firstname.value = user.firstname.toString()
                }
                else
                {
                    error("user doesn't exist")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                error("user doesn't exist")
            }
        })
    }

    /**
     * Load all treatments that are planned for today
     */
    fun loadTreatments() {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(TreatmentService::class.java)

        // get query from Rest service
        val treatmentRequest = service?.getAllByIdUser(Utils.userId)

        // enqueue request response
        treatmentRequest?.enqueue(object : Callback<List<Treatment>> {
            override fun onResponse(call: Call<List<Treatment>>, response: Response<List<Treatment>>) {

                // take user model from response
                val listTreatment = response.body()

                // if user is not null (if user exist)
                if (listTreatment != null)
                {
                    // update data
                    treatment.value = listTreatment!!
                }
            }

            override fun onFailure(call: Call<List<Treatment>>, t: Throwable) {
                error(t.message.toString())
            }
        })
    }

    /**
     * Load last measure
     */
    fun loadMeasure() {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(MeasureService::class.java)

        // get last measure for user from Rest service
        val measureRequest = service?.getLastByIdUser(Utils.userId)

        // enqueue request response
        measureRequest?.enqueue(object : Callback<Measure> {
            override fun onResponse(call: Call<Measure>, response: Response<Measure>) {

                // take measure model from response
                val lastmeasure = response.body()

                // if user is not null (if user exist)
                if (lastmeasure != null)
                {
                    // update data
                    measure.value = lastmeasure!!
                }
            }

            override fun onFailure(call: Call<Measure>, t: Throwable) {
                //error ignored
            }
        })
    }

    fun takeTreatment(treatment: Treatment) {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(TreatmentService::class.java)

        // update treatment in Rest service
        val treatmentRequest = service?.update(treatment.id!!, treatment)

        treatmentRequest?.enqueue(object : Callback<Treatment>
        {
            override fun onResponse(call: Call<Treatment>, response: Response<Treatment>) {
                val treatmentUpdate = response.body() ?: error("error on update treatment")
            }

            override fun onFailure(call: Call<Treatment>, t: Throwable) {
                error(t.message.toString())
            }

        })
    }
}