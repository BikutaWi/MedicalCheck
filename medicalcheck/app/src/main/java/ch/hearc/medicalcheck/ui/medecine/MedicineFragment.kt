package ch.hearc.medicalcheck.ui.medecine

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.AddMedicineActivity
import ch.hearc.medicalcheck.R
import ch.hearc.medicalcheck.UpdateMedicineActivity
import java.sql.Timestamp

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class MedicineFragment : Fragment() {

    companion object {
        fun newInstance() = MedicineFragment()
    }

    private lateinit var viewModel: MedicineViewModel
    private lateinit var customAdapter: MedicineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_medecine, container, false)

        viewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)

        // "Add" button listener
        val buttonAdd: Button = view.findViewById(R.id.btnAddMedicine)
        buttonAdd.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddMedicineActivity::class.java)
            //startActivity(intent)

            //start add medicine activity and wait for result
            startActivityForResult(intent, 1)
        })

        // when view receive medicine
        viewModel.getItems().observe(viewLifecycleOwner, Observer {
            val recyclerView: RecyclerView = view.findViewById(R.id.medicinesRecyclerView)
            customAdapter = MedicineAdapter(it)

            // on medicine item click
            customAdapter.onItemClick = { medicine ->
                val intent = Intent(activity, UpdateMedicineActivity::class.java)

                // send data to other activity
                intent.putExtra("medicine", medicine.name)
                intent.putExtra("dose", medicine.dose)
                intent.putExtra("id", medicine.id)
                intent.putExtra("startNow", medicine.begindate?.before(Timestamp(System.currentTimeMillis())))

                // wait for result to update view
                startActivityForResult(intent, 1)
            }

            val layoutManager = LinearLayoutManager(activity?.applicationContext)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter
        })

        // load medicine list
        viewModel.loadMedicines()

        return view
    }

    /***
     * when activity get result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // reload user to get new information data
        viewModel.loadMedicines()
    }
}