package ch.hearc.medicalcheck.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.models.Treatment
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

internal class HomeAdapter(private var itemsList: ArrayList<Treatment>) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    // enable on click event
    var onItemClick: ((Treatment) -> Unit)? = null

    // MyViewHolder object
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tbxMedicineName: TextView = view.findViewById(R.id.reminderName)
        var tbxTime: TextView = view.findViewById(R.id.reminderTime)
        var tbxDose: TextView = view.findViewById(R.id.reminderDose)
        var card: CardView = view.findViewById(R.id.reminderCard)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(itemsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.tbxMedicineName.text = item.planning?.medicine?.name
        holder.tbxTime.text = item.planning?.time?.dropLast(3)
        holder.tbxDose.text = item.planning?.quantity.toString() + " x " + item.planning?.medicine?.dose.toString()

        // if planning time is after local time, planning is not already forgotten (show green)
        if (LocalTime.now(Clock.system(ZoneId.of("UTC+1"))).isBefore(LocalTime.parse(item.planning?.time))) {
            holder.card.setCardBackgroundColor(Color.parseColor("#77DD77"))
        }
        // planning get over time, user forgot to take medicine (show red)
        else {
            holder.card.setCardBackgroundColor(Color.parseColor("#FB335B"))
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun removeItem(treatment: Treatment) {
        itemsList.remove(treatment)
    }
}