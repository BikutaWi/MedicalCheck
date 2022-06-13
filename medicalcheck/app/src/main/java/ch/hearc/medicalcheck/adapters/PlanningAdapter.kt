package ch.hearc.medicalcheck.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.models.Planning
import ch.hearc.medicalcheck.services.PlanningService
import ch.hearc.medicalcheck.utils.Utils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

internal class PlanningAdapter(private var itemsList: ArrayList<Planning>, private var context: Context) :
    RecyclerView.Adapter<PlanningAdapter.MyViewHolder>() {

    // on click item event
    var onItemClick: ((Planning) -> Unit)? = null

    // view holder object
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tbxDay: TextView = view.findViewById(R.id.txtPlanningDay)
        var tbxHour: TextView = view.findViewById(R.id.txtPlanningHour)
        var tbxQuantity: TextView = view.findViewById(R.id.txtPlanningQuantity)
        var btnDelete: Button = view.findViewById(R.id.btnDeletePlanning)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.planning_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]

        // if item's day is null, that means planning date is everyday
        if(item.day == null) {
            holder.tbxDay.text = "EVERYDAY"
        } else {
            holder.tbxDay.text = item.day.toString()
        }

        holder.tbxHour.text = item.time?.dropLast(3);
        holder.tbxQuantity.text = item.quantity.toString() + " dose";

        // on delete click
        holder.btnDelete.setOnClickListener {

            // create alert dialog
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to delete this planning")
                .setCancelable(false)
                    // on "yes" click
                .setPositiveButton("Yes") { dialog, id ->
                    // get retrofit instance and create service
                    val retrofit = Utils.retrofitInstance
                    val service = retrofit?.create(PlanningService::class.java)

                    // delete query from Rest service
                    val planningRequest = service?.delete(item.id!!)
                    // enqueue query response
                    planningRequest?.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            itemsList.remove(item)
                            notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // user has no measure
                            // error is ignored
                        }
                    })
                }
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
}