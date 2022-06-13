package ch.hearc.medicalcheck

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import ch.hearc.medicalcheck.utils.Utils
import ch.hearc.medicalcheck.viewmodels.*
import java.util.*
import java.sql.Time
import java.sql.Timestamp
import java.time.DayOfWeek
import androidx.lifecycle.Observer
import ch.hearc.medicalcheck.models.Planning
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class AddMedicineActivity : AppCompatActivity() {

    lateinit var viewModel: AddMedicineViewModel

    lateinit var spinnerDays: Spinner
    lateinit var startDate: TextView
    lateinit var startDateListener: DatePickerDialog.OnDateSetListener
    lateinit var endDate: TextView
    lateinit var endDateListener: DatePickerDialog.OnDateSetListener
    lateinit var timeListener: TimePickerDialog.OnTimeSetListener
    lateinit var hour: TextView

    var h: Int = 12
    var m: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar on activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_add_medicine)

        // create array adapter or spinner input
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, Utils.getDays()
        )

        // add array adapter for spinner input
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDays = findViewById(R.id.spinnerDay)
        spinnerDays.adapter = adapter

        // on start date button click
        // create a DatePickerDialog and show it
        startDate = findViewById(R.id.btnStartDate)
        startDate.setOnClickListener ( View.OnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]

            val dialog = DatePickerDialog(
                this@AddMedicineActivity,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                startDateListener,
                year, month, day)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        })

        // On date set listener
        // update button text with chosen date
        startDateListener = OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date = "$month/$day/$year"
                startDate.text = date
        }

        // on end date button click
        // create a DatePickerDialog and show it
        endDate = findViewById(R.id.btnEndDate)
        endDate.setOnClickListener ( View.OnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]

            val dialog = DatePickerDialog(
                this@AddMedicineActivity,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                endDateListener,
                year, month, day)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        })

        // On date set listener
        // update button text with chosen date
        endDateListener = OnDateSetListener { datePicker, year, month, day ->
            var month = month
            month += 1
            val date = "$month/$day/$year"
            endDate.text = date
        }

        // on start date button click
        // create a TimePickerDialog and show it
        hour = findViewById(R.id.btnHour)
        hour.setOnClickListener (View.OnClickListener {
            val timePickerDialog = TimePickerDialog(this@AddMedicineActivity, AlertDialog.THEME_HOLO_LIGHT, timeListener, h, m, true);

            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        })

        // On date set listener
        // update button text with chosen time
        timeListener = OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            h = selectedHour
            m = selectedMinute
            hour.setText(java.lang.String.format(Locale.getDefault(),"%02d:%02d", h, m))
        }

        // finish activity on "back" button click listener
        val btnBack: Button = findViewById(R.id.btnBackNewMedicine)
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        // call add function on "add" button click listner
        val buttonAdd: Button = findViewById(R.id.btnAddNewMedicine)
        buttonAdd.setOnClickListener(View.OnClickListener {
            add()
        })
    }

    /***
     * Show status message
     */
    fun showMessage(message: String) {
        Toast.makeText(this@AddMedicineActivity, message, Toast.LENGTH_LONG).show()
    }

    /***
     * add new medicine in database
     */
    fun add() {

        val tbxMedecineName: TextView = findViewById(R.id.editMedecineName)
        val tbxDose: TextView = findViewById(R.id.editDose)
        val tbxQuantity: TextView = findViewById(R.id.editQuantity)

        // check required inputs
        if( tbxMedecineName.text.toString().isEmpty() ||
            tbxDose.text.toString().isEmpty() ||
            tbxQuantity.text.toString().isEmpty() ||
            startDate.text.toString().isEmpty() ||
            hour.text.toString().isEmpty())
        {
            showMessage("Please fill all required fields")
        }
        else
        {
            val medecineName: String = tbxMedecineName.text.toString()
            val dose: String = tbxDose.text.toString()
            val quantity: Int = tbxQuantity.text.toString().toInt()

            // parse start date to timestamp format
            val start_date: Timestamp = parseDate(startDate.text.toString())

            var end_date: Timestamp? = null

            // if user introduce end date
            if(endDate.text.toString() != "Pick a date")
            {
                // parse end date to timestamp format
                end_date = parseDate(endDate.text.toString())

                // check that start date is not after end date
                if(start_date.after(end_date))
                {
                    showMessage("Error: Planning start after end date")
                    return
                }
            }

            // parse time to Time format
            val time: Time = parseTime(hour.text.toString())

            // parse spinner day to DayOfWeek value
            var day: DayOfWeek? = null
            if(spinnerDays.selectedItem.toString() != "Everyday")
                day = DayOfWeek.valueOf(spinnerDays.selectedItem.toString().toUpperCase())

            // create viewmodel
            viewModel = ViewModelProvider(this).get(AddMedicineViewModel::class.java)

            // create observer on state message
            viewModel.getState().observe(this, Observer { updateUi(it!!) })

            // create observer for medicine id
            // if viewmodel insert medicine successfully call add planning function
            viewModel.getMedicine().observe(this, Observer {
                var planning: Planning = Planning()
                planning.idmedicine = it.id
                planning.day = day
                //planning.time = time
                planning.time = time.toString()
                planning.quantity = quantity

                viewModel.createPlanning(planning, it.begindate?.before(Timestamp(System.currentTimeMillis()))!!)
            })

            // call create medicine function
            viewModel.create(medecineName, dose, start_date, end_date)
        }
    }

    /***
     * Parse date to Timestamp format
     */
    private fun parseDate(s: String): Timestamp {

        var str: String = ""
        val strs = s.split("/").toTypedArray()

        for (i in 0..1)
        {
            if (strs[i].length == 1 )
                str = str + "0" + strs[i]
            else
                str += strs[i]

            str += "/"
        }

        str += strs[2]

        var l = LocalDate.parse(str, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        var unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val date: Timestamp = Timestamp(unix)

        return date
    }

    /***
     * Parse time to Time format
     */
    private fun parseTime(s: String): Time {
        val strs = s.split(":").toTypedArray()

        return Time(strs[0].toInt(), strs[1].toInt(), 0)
    }

    /***
     * update activity when state is success or failure
     */
    private fun updateUi(state: AddMedicineViewModelState) {
        return when(state) {
            is AddMedicineViewModelStateSuccess -> {
                showMessage(state.message)
                finish()
            }
            is AddMedicineViewModelStateFailure -> {
                showMessage(state.message)
            }
            else -> {
                showMessage("Fatal error !")
            }
        }
    }
}