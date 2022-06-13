package ch.hearc.medicalcheck

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.medicalcheck.adapters.PlanningAdapter
import ch.hearc.medicalcheck.models.Follow
import ch.hearc.medicalcheck.models.Planning
import ch.hearc.medicalcheck.models.User
import ch.hearc.medicalcheck.services.PlanningService
import ch.hearc.medicalcheck.services.UsersService
import ch.hearc.medicalcheck.ui.medecine.MedicineAdapter
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.viewmodels.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String
import java.sql.Time
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class UpdateMedicineActivity : AppCompatActivity() {

    lateinit var viewModel: UpdateMedicineViewModel
    private lateinit var customAdapter: PlanningAdapter

    lateinit var spinnerDays: Spinner
    lateinit var timeListener: TimePickerDialog.OnTimeSetListener
    lateinit var hour: Button
    var h: Int = 12
    var m: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar on activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_update_medicine)

        val tbxMedicine: TextView = findViewById(R.id.tbxMedicineName)
        val tbxDose: TextView = findViewById(R.id.tbxDose)
        tbxMedicine.text = intent.getStringExtra("medicine")
        tbxDose.text = intent.getStringExtra("dose")

        // on start date button click
        // create a TimePickerDialog and show it
        hour = findViewById(R.id.btnHourPlanning)
        hour.setOnClickListener (View.OnClickListener {
            val timePickerDialog = TimePickerDialog(this@UpdateMedicineActivity, AlertDialog.THEME_HOLO_LIGHT, timeListener, h, m, true);

            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        })

        // On date set listener
        // update button text with chosen time
        timeListener = TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                h = selectedHour
                m = selectedMinute
                hour.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m))
        }

        // create array adapter or spinner input
        val adapter: ArrayAdapter<kotlin.String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, Utils.getDays()
        )

        // add array adapter for spinner input
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDays = findViewById(R.id.spinnerNewDay)
        spinnerDays.adapter = adapter

        // finish activity on "back" button click listener
        val btnBack: Button = findViewById(R.id.btnBackUpdateMedicine)
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        // call add function on "add" button click listner
        val buttonAdd: Button = findViewById(R.id.btnAddPlanning)
        buttonAdd.setOnClickListener(View.OnClickListener {
            add()
        })

        viewModel = ViewModelProvider(this).get(UpdateMedicineViewModel::class.java)
        viewModel.getState().observe(this, Observer {updateUi(it!!)})

        val btnDelete: Button = findViewById(R.id.btnDeleteMedicine)

        // on delete click
        btnDelete.setOnClickListener {

            // create alert dialog
            val builder = AlertDialog.Builder(this@UpdateMedicineActivity)
            builder.setMessage("Are you sure you want to delete this medicine")
                .setCancelable(false)

                    // on "yes" click
                .setPositiveButton("Yes") { dialog, id ->
                    // delete medicine
                    viewModel.delete(intent.getIntExtra("id", 0))
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        // update view when items list update
        viewModel.getItems().observe(this, Observer {
            val recyclerView: RecyclerView = findViewById(R.id.planningRecyclerView)
            customAdapter = PlanningAdapter(it as ArrayList<Planning>, this@UpdateMedicineActivity)

            val layoutManager = LinearLayoutManager(this@UpdateMedicineActivity?.applicationContext)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter
        })

        viewModel.loadPlannings(intent.getIntExtra("id", 0))
    }

    /***
     * Show status message
     */
    fun showMessage(message: kotlin.String) {
        Toast.makeText(this@UpdateMedicineActivity, message, Toast.LENGTH_LONG).show()
    }

    /***
     * add new planning in database
     */
    private fun add() {
        val tbxQuantity: TextView = findViewById(R.id.newQuantity)

        if(tbxQuantity.text.isEmpty() || hour.text.toString().toUpperCase().equals("PICK AN HOUR"))
        {
            showMessage("Please fill all required fields")
        }
        else
        {
            var day: DayOfWeek? = null
            if(spinnerDays.selectedItem.toString() != "Everyday")
                day = DayOfWeek.valueOf(spinnerDays.selectedItem.toString().toUpperCase())

            // parse time to Time format
            val time: Time = parseTime(hour.text.toString())

            val quantity: Int = tbxQuantity.text.toString().toInt()

            val id: Int = intent.getIntExtra("id", 0)

            viewModel.create(id, time.toString(), day, quantity, intent.getBooleanExtra("startNow", false))
        }
    }

    /***
     * Parse time to Time format
     */
    private fun parseTime(s: kotlin.String): Time {
        val strs = s.split(":").toTypedArray()

        return Time(strs[0].toInt(), strs[1].toInt(), 0)
    }

    /***
     * update activity when state is success or failure
     */
    private fun updateUi(state: UpdateMedicineViewModelState) {
        return when(state) {
            is UpdateMedicineViewModelStateSuccess -> {
                showMessage(state.message)
                finish()
            }
            is UpdateMedicineViewModelStateFailure -> {
                showMessage(state.message)
            }
            is UpdateMedicineViewModelStateDelete -> {
                showMessage(state.message)
                finish()
            }
            else -> {
                showMessage("Fatal error !")
            }
        }
    }
}