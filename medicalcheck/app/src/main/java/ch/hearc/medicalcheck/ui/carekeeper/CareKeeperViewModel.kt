package ch.hearc.medicalcheck.ui.carekeeper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.models.Follow
import ch.hearc.medicalcheck.services.FollowService
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

class CareKeeperViewModel : ViewModel() {
    private val items = MutableLiveData<List<Follow>>()
    fun getItems() : LiveData<List<Follow>> = items

    fun loadFollows() {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(FollowService::class.java)

        // get follows relation from Rest service
        val followRequest = service?.getAllFollowByIdUserCareKeeper(Utils.userId)

        // enqueue query response
        followRequest?.enqueue(object : Callback<List<Follow>> {
            override fun onResponse(call: Call<List<Follow>>, response: Response<List<Follow>>) {

                // take user list from query response
                val list = response.body()

                // if list is not null
                if (list != null)
                {
                    // update data
                    items.value = list!!
                }
            }

            override fun onFailure(call: Call<List<Follow>>, t: Throwable) {
                error("patient doesn't exist")
            }
        })
    }
}