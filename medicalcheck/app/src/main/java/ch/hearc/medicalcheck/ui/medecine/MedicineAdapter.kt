package ch.hearc.medicalcheck.ui.medecine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.models.Medicine

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

internal class MedicineAdapter(private var itemsList: List<Medicine>) :
    RecyclerView.Adapter<MedicineAdapter.MyViewHolder>() {

    // enable item click event
    var onItemClick: ((Medicine) -> Unit)? = null

    // MyViewHoldre object
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tbxMedicineName: TextView = view.findViewById(R.id.txtPlanningDay)
        var tbxMeasure: TextView = view.findViewById(R.id.txtPlanningHour)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(itemsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.medicine_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.tbxMedicineName.text = item.name
        holder.tbxMeasure.text = item.dose;

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}