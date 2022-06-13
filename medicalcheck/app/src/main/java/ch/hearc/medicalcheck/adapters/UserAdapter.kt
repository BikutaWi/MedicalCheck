package ch.hearc.medicalcheck.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.models.Treatment
import ch.hearc.medicalcheck.models.User

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

internal class UserAdapter(private var itemsList: List<User>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>(), Filterable {

    // temporary list for filtering
    private var myFilterList:List<User> = ArrayList<User>()
    private var tmpList:List<User> = ArrayList<User>()

    // enable on item click event
    var onItemClick: ((User) -> Unit)? = null

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tbxName: TextView = view.findViewById(R.id.tbxCareKeeperName)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(itemsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.carekeeper_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]

        holder.tbxName.text = item.firstname + " " + item.lastname
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    /**
     * Perform filter
     */
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(text: CharSequence?): FilterResults {

                if(tmpList.count() > 0) {
                    itemsList = tmpList
                }

                val textSearch: String = text.toString()

                if(textSearch.isEmpty()) {
                    myFilterList = itemsList
                }
                else {
                    myFilterList = itemsList.filter {
                        val name : String = it.firstname + " " + it.lastname

                        //try to match filter with name
                        name.contains(textSearch)
                    }
                }

                // set filter result
                val filterResults: FilterResults = FilterResults()
                filterResults.values = myFilterList
                return filterResults
            }

            override fun publishResults(text: CharSequence?, results: FilterResults?) {
                tmpList = itemsList
                itemsList = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }

        }
    }
}