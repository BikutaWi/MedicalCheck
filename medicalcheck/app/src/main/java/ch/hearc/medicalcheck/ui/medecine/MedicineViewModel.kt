package ch.hearc.medicalcheck.ui.medecine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.models.Medicine
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.MedicineService
import ch.hearc.medicalcheck.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class MedicineViewModel : ViewModel() {
    private val items = MutableLiveData<List<Medicine>>()
    fun getItems() : LiveData<List<Medicine>> = items

    fun loadMedicines() {
        // get connected user's id
        val userid: Int = Utils.userId

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(MedicineService::class.java)

        // get query from Rest service
        val userRequest = service?.getAllByIdUser(userid)

        // enqueue query response
        userRequest?.enqueue(object : Callback<List<Medicine>> {
            override fun onResponse(call: Call<List<Medicine>>, response: Response<List<Medicine>>) {

                // take user model from query response
                val list = response.body()

                // if user exist
                if (list != null)
                {
                    // update data
                    items.value = list!!
                }
            }

            override fun onFailure(call: Call<List<Medicine>>, t: Throwable) {
                error("user doesn't exist")
            }
        })
    }
}