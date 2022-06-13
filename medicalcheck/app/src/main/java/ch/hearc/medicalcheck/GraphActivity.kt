package ch.hearc.medicalcheck

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import ch.hearc.medicalcheck.viewmodels.GraphViewModel
import androidx.lifecycle.Observer
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class GraphActivity : AppCompatActivity() {

    lateinit var viewModel: GraphViewModel

    lateinit var btnDate: Button
    lateinit var dateListener: OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar on activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_graph)

        // finish activity on "back" button click listener
        val btnBack: Button = findViewById(R.id.btnBackGraph)
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        viewModel = GraphViewModel()

        btnDate = findViewById(R.id.btnGraphDate)

        // init date button
        val cal: Calendar = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]+1
        val day = cal[Calendar.DAY_OF_MONTH]
        btnDate.text = "$year-$month-$day"

        // on date button click
        // create a DatePickerDialog and show it
        btnDate.setOnClickListener ( View.OnClickListener {
            val split = btnDate.text.toString().split("-")
            val year = split[0].toInt()
            val month = split[1].toInt()-1
            val day = split[2].toInt()

            val dialog = DatePickerDialog(
                this@GraphActivity,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateListener,
                year, month, day)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        })

        // On date set listener
        // update button text with chosen date
        dateListener = OnDateSetListener { datePicker, year, month, day ->
            var month = month
            month += 1
            val date = "$year-$month-$day"
            btnDate.text = date

            viewModel.loadMeasures(date)
        }

        val graph: GraphView = findViewById(R.id.graph) as GraphView

        // update graph when measure list update
        viewModel.getMeasures().observe(this, Observer {

            val tab = arrayOfNulls<DataPoint>(it.size)
            graph.removeAllSeries()

            // create data point array
            var count = 0
            for ((k, v) in it) {
                tab[count++] = DataPoint(k.toDouble(), v)
            }

            val series: LineGraphSeries<DataPoint> = LineGraphSeries(tab)

            // set date label formatter
            graph.gridLabelRenderer.numHorizontalLabels = 12

            graph.viewport.isScalable = false

            // set manual x bounds to have nice steps
            graph.viewport.setMinX(0.0)
            graph.viewport.setMaxX(23.0)
            graph.viewport.isXAxisBoundsManual = true

            graph.addSeries(series)
            graph.onDataChanged(true, true)
        })

        viewModel.loadMeasures(btnDate.text.toString())
    }
}