package ch.hearc.medicalcheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hearc.medicalcheck.models.Follow
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.FollowService
import ch.hearc.medicalcheck.services.UsersService
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

class AddCareKeeperViewModel : ViewModel() {

    private val items = MutableLiveData<List<User>>()
    fun getItems() : LiveData<List<User>> = items

    private val followData = MutableLiveData<Follow>()
    fun getFollow() : LiveData<Follow> = followData

    /**
     * Load carekeeper list
     */
    fun loadUsers() {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(UsersService::class.java)

        // get available carekeeper from Rest service
        val userRequest = service?.getAllAvailableCarekeeper(Utils.userId)

        // enqueue query response
        userRequest?.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                // take user list from query response
                val list = response.body()

                // if list exist
                if (list != null)
                {
                    // update data
                    items.value = list!!
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                error("user doesn't exist")
            }
        })
    }

    /**
     * create relation
     */
    fun createFollow(follow: Follow) {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(FollowService::class.java)

        // create follow in Rest service
        val followRequest = service?.create(follow)

        // enqueue query response
        followRequest?.enqueue(object : Callback<Follow> {
            override fun onResponse(call: Call<Follow>, response: Response<Follow>) {

                // take user list from query response
                val newFollow = response.body()

                // if user exist
                if (newFollow != null)
                {
                    // update data
                    followData.value = newFollow!!
                }
            }

            override fun onFailure(call: Call<Follow>, t: Throwable) {
                error("fail to create relation")
            }
        })
    }
}