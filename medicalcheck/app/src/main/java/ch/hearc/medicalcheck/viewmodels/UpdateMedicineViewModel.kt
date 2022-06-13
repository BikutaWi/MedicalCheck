package ch.hearc.medicalcheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.models.Medicine
import ch.hearc.medicalcheck.models.Planning
import ch.hearc.medicalcheck.models.Treatment
import ch.hearc.medicalcheck.services.MedicineService
import ch.hearc.medicalcheck.services.PlanningService
import ch.hearc.medicalcheck.services.TreatmentService
import ch.hearc.medicalcheck.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.sql.Timestamp
import java.time.DayOfWeek
import java.time.LocalDate

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

sealed class UpdateMedicineViewModelState (val message: String = "")

object UpdateMedicineViewModelStateSuccess : UpdateMedicineViewModelState("Planning created successfully")

object UpdateMedicineViewModelStateDelete : UpdateMedicineViewModelState("Medicine deleted")

object UpdateMedicineViewModelStateFailure : UpdateMedicineViewModelState("Planning not created, an error occured")

class UpdateMedicineViewModel : ViewModel() {

    private val state = MutableLiveData<UpdateMedicineViewModelState>()
    fun getState() : LiveData<UpdateMedicineViewModelState> = state

    private val items = MutableLiveData<List<Planning>>()
    fun getItems() : LiveData<List<Planning>> = items

    /**
     * Load medicine's planning
     */
    fun loadPlannings(idMedicine: Int) {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(PlanningService::class.java)

        // get query from Rest service
        val userRequest = service?.getAllByIdMedicine(idMedicine)

        // enqueue query response
        userRequest?.enqueue(object : Callback<List<Planning>> {
            override fun onResponse(call: Call<List<Planning>>, response: Response<List<Planning>>) {

                // take user model from query response
                val list = response.body()

                // if user exist
                if (list != null)
                {
                    // update data
                    items.value = list!!
                }
            }

            override fun onFailure(call: Call<List<Planning>>, t: Throwable) {
                error("medicine doesn't exist")
            }
        })
    }

    /**
     * create new planning
     */
    fun create(medicineId: Int, time: String, day: DayOfWeek?, quantity: Int, startNow: Boolean) {

        // create planning model
        var planning: Planning = Planning()
        planning.idmedicine = medicineId
        planning.day = day
        planning.time = time.toString()
        planning.quantity = quantity

        createPlanning(planning, startNow)
    }

    /**
     * Create planning in database
     */
    private fun createPlanning(planning: Planning, startNow: Boolean) {
        try {
            // get retrofit instance and create service
            val retrofit = Utils.retrofitInstance
            val planningService = retrofit?.create(PlanningService::class.java)

            // create query
            val planningRequest = planningService.create(planning)

            // enqueue query response
            planningRequest?.enqueue(object : Callback<Planning> {
                override fun onResponse(call: Call<Planning>, response: Response<Planning>) {

                    // take new planning model from query reponse
                    val planningNew = response.body()

                    // if new planning exists
                    if (planningNew != null)
                    {
                        // if planning day of week is today or everyday
                        if((planningNew.day?.name == LocalDate.now().dayOfWeek.name || planningNew.day == null) && startNow) {

                            // create new treatment
                            createTreatment(planningNew.id!!)
                        }

                        state.value = UpdateMedicineViewModelStateSuccess
                    }
                    else
                    {
                        state.value = UpdateMedicineViewModelStateFailure
                    }
                }

                override fun onFailure(call: Call<Planning>, t: Throwable) {
                    //state.value = AddMedicineViewModelStateFailure

                    // rest create planning successfully but always go on failure !!!
                    state.value = UpdateMedicineViewModelStateSuccess
                }
            })
        }
        catch (e: Exception) {
            state.value = UpdateMedicineViewModelStateFailure
        }
    }

    /**
     * create treatment if planning is for today
     */
    private fun createTreatment(idPlanning: Int) {
        var treatment: Treatment = Treatment()
        treatment.istaken = false
        treatment.date = Timestamp(System.currentTimeMillis())
        treatment.idplanning = idPlanning

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val treatmentService = retrofit?.create(TreatmentService::class.java)

        // create query
        val treatmentRequest = treatmentService.create(treatment)

        // enqueue query response
        treatmentRequest?.enqueue(object : Callback<Treatment> {
            override fun onResponse(call: Call<Treatment>, response: Response<Treatment>) {

                // take response
                val treatmentNew = response.body()
            }

            override fun onFailure(call: Call<Treatment>, t: Throwable) {
                // error ignored
            }
        })
    }

    /**
     * Delete medicine
     */
    fun delete(medicineId: Int) {
        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(MedicineService::class.java)

        // delete from Rest service
        val medicineRequest = service?.delete(medicineId)
        // enqueue query response
        medicineRequest?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                state.value = UpdateMedicineViewModelStateDelete
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // user has no measure
                // error is ignored
            }
        })
    }
}