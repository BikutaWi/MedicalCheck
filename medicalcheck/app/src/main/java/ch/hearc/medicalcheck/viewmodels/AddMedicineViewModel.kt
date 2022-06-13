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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.sql.Timestamp
import java.time.LocalDate

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

sealed class AddMedicineViewModelState (val message: String = "")

object AddMedicineViewModelStateSuccess : AddMedicineViewModelState("Medecine created successfully")

object AddMedicineViewModelStateFailure : AddMedicineViewModelState("Medicine not created, an error occured")

class AddMedicineViewModel : ViewModel() {

    private val state = MutableLiveData<AddMedicineViewModelState>()
    fun getState() : LiveData<AddMedicineViewModelState> = state
    private val newMedicine = MutableLiveData<Medicine>()
    fun getMedicine() : LiveData<Medicine> = newMedicine

    fun create(medicineName: String,
               dose: String,
               start_date: Timestamp,
               end_date: Timestamp?) {

        // create medicine model
        var medicine: Medicine = Medicine()
        medicine.iduser = Utils.userId
        medicine.name = medicineName
        medicine.begindate = start_date
        medicine.enddate = end_date
        medicine.dose = dose

        createMedicine(medicine)
    }

    /***
     * insert new medicine into database
     */
    fun createMedicine(medicine: Medicine) {
        try {
            // get retrofit instance and create service
            val retrofit = Utils.retrofitInstance
            val medicineService = retrofit?.create(MedicineService::class.java)

            // create query
            val medicineRequest = medicineService.create(medicine)

            // enqueue query response
            medicineRequest?.enqueue(object : Callback<Medicine> {

                override fun onResponse(call: Call<Medicine>, response: Response<Medicine>) {

                    // take medicine model from query response
                    val medicineNew = response.body()

                    // if medicine exists
                    if (medicineNew != null)
                    {
                        // update medicine id to call observer and create planning
                        newMedicine.value = medicineNew!!
                    }
                    else
                    {
                        state.value = AddMedicineViewModelStateFailure
                    }
                }

                override fun onFailure(call: Call<Medicine>, t: Throwable) {
                    state.value = AddMedicineViewModelStateFailure
                }
            })
        }
        catch (e: Exception) {
            error(e)
            state.value = AddMedicineViewModelStateFailure
        }
    }

    /***
     * Insert new planning into database
     */
    fun createPlanning(planning: Planning, startNow: Boolean) {
        try {
            // get retrofit instance and create service
            val retrofit = Utils.retrofitInstance
            val planningService = retrofit?.create(PlanningService::class.java)

            // create query
            val planningRequest = planningService.create(planning)

            // enqueue query response
            planningRequest?.enqueue(object : Callback<Planning> {
                override fun onResponse(call: Call<Planning>, response: Response<Planning>) {

                    // take new planning model from query response
                    val planningNew = response.body()

                    // if new planning exists
                    if (planningNew != null)
                    {
                        // if planning day of week is today or everyday
                        if((planningNew.day?.name == LocalDate.now().dayOfWeek.name || planningNew.day == null) && startNow) {

                            // create new treatment
                            createTreatment(planningNew.id!!)
                        }

                        state.value = AddMedicineViewModelStateSuccess
                    }
                    else
                    {
                        state.value = AddMedicineViewModelStateFailure
                    }
                }

                override fun onFailure(call: Call<Planning>, t: Throwable) {
                    //state.value = AddMedicineViewModelStateFailure

                    // rest create planning successfully but always go on failure !!!
                    state.value = AddMedicineViewModelStateSuccess
                }
            })
        }
        catch (e: Exception) {
            state.value = AddMedicineViewModelStateFailure
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
}