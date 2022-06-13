package ch.hearc.medicalcheck.ui.carekeeper

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.models.Follow
import ch.hearc.medicalcheck.models.Measure
import ch.hearc.medicalcheck.models.Planning
import ch.hearc.medicalcheck.services.FollowService
import ch.hearc.medicalcheck.services.MeasureService
import ch.hearc.medicalcheck.services.PlanningService
import ch.hearc.medicalcheck.services.TreatmentService
import ch.hearc.medicalcheck.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import android.app.ActivityManager
import android.content.Context

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

internal class CareKeeperAdapter(private var itemsList: ArrayList<Follow>, private var context: Context) :

    RecyclerView.Adapter<CareKeeperAdapter.MyViewHolder>() {

    // MyViewHolder object
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tbxUserName: TextView = view.findViewById(R.id.tbxUserName)
        var tbxRelationName: TextView = view.findViewById(R.id.tbxRelationName)
        var tbxUserHeartBeat: TextView = view.findViewById(R.id.tbxUserHeartBeat)
        var tbxUserMedicineDate: TextView = view.findViewById(R.id.tbxUserMedicineDate)
        var imgUserTreatment: ImageView = view.findViewById(R.id.imgUserTreatment)
        var btnDelete: Button = view.findViewById(R.id.btnDeletePatient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.tbxUserName.text = item.userpatient?.firstname + " " + item.userpatient?.lastname
        holder.tbxRelationName.text = item.relationship

        loadMeasure(item.iduserpatient!!, holder)
        loadPlanning(item.iduserpatient!!, holder)
        loadTreatment(item.iduserpatient!!, holder)

        // on delete button click
        holder.btnDelete.setOnClickListener {

            // show an alert dialog
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to delete this patient")
                .setCancelable(false)

                    //On "yes" click
                .setPositiveButton("Yes") { dialog, id ->
                    // get retrofit instance and create service
                    val retrofit = Utils.retrofitInstance
                    val service = retrofit?.create(FollowService::class.java)

                    // delete query from Rest service
                    val followRequest = service?.delete(item.id!!)
                    // enqueue query response
                    followRequest?.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                            //Update recycler view
                            itemsList.remove(item)
                            notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // user has no measure
                            // error is ignored
                        }
                    })
                }
                    // "no" click
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    /**
     * Load measure for user
     */
    private fun loadMeasure(userid: Int, holder: MyViewHolder) {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(MeasureService::class.java)

        // get last measure from Rest service
        val measureRequest = service?.getLastByIdUser(userid)
        // enqueue query response
        measureRequest?.enqueue(object : Callback<Measure> {
            override fun onResponse(call: Call<Measure>, response: Response<Measure>) {

                // take measure from query response
                val newMeasure = response.body()

                // if measure is not null (if measure exist)
                if (newMeasure != null)
                {
                    val date = newMeasure!!.date
                    date!!.hours = date.hours-1

                    // update holder
                    holder.tbxUserHeartBeat.text = newMeasure!!.heartrate.toString() + " bpm / " + date.toString().dropLast(7)
                }
            }

            override fun onFailure(call: Call<Measure>, t: Throwable) {
                // user has no measure
                // error is ignored
            }
        })
    }

    /**
     * Load plannings hour for user
     */
    private fun loadPlanning(userid: Int, holder: MyViewHolder) {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(PlanningService::class.java)

        // get all today's planning for user from Rest service
        val planningRequest = service?.getAllTodayByIdUser(userid, LocalDate.now().dayOfWeek.toString())

        // enqueue query response
        planningRequest?.enqueue(object : Callback<List<Planning>> {
            override fun onResponse(call: Call<List<Planning>>, response: Response<List<Planning>>) {

                // take list from query response
                val list = response.body()

                // if list is not null
                if (list != null)
                {
                    var s: String = ""

                    // update holder
                    for (planning: Planning in list) {

                        // if it's first planning
                        if(s.isNotEmpty()) {
                            s += ", "
                        }

                        s += planning.time.toString().dropLast(3)
                    }

                    if(s.isNotEmpty())
                        holder.tbxUserMedicineDate.text = s
                }
            }

            override fun onFailure(call: Call<List<Planning>>, t: Throwable) {
                // user has no treatment today
                // error is ignored
            }
        })
    }

    /**
     * Load treatment for user (update status)
     */
    private fun loadTreatment(userid: Int, holder: MyViewHolder) {

        // get retrofit instance and create service
        val retrofit = Utils.retrofitInstance
        val service = retrofit?.create(TreatmentService::class.java)

        // get count treatment from Rest service
        val treatmentRequest = service?.countTreatmentNotTaken(userid)

        // enqueue query response
        treatmentRequest?.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {

                // take int from query response
                val count = response.body()

                // if count is not null
                if (count != null) {

                    // if count > 0, user forgot treatment
                    if (count > 0) {
                        holder.imgUserTreatment.setImageResource(R.drawable.ic_cross)
                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                // user has no treatment today
                // error is ignored
            }
        })
    }
}