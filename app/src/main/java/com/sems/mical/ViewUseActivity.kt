package com.sems.mical

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.sems.mical.data.AppDatabase
import com.sems.mical.data.entities.MicrophoneIsBeingUsed
import kotlinx.android.synthetic.main.activity_view_use.*

class ViewUseActivity : AppCompatActivity() {

        lateinit var skillRatingChart : HorizontalBarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_use)

        setSkillGraph( )

    }


    /**
     * Set up the axes along with other necessary details for the horizontal bar chart.
     */
    fun setSkillGraph(){
        skillRatingChart = skill_rating_chart              //skill_rating_chart is the id of the XML layout

        skillRatingChart.setDrawBarShadow(false)
        val description = Description()
        description.text = ""
        skillRatingChart.description = description
        skillRatingChart.legend.setEnabled(false)
        skillRatingChart.setPinchZoom(false)
        skillRatingChart.setDrawValueAboveBar(false)

        //Display the axis on the left (contains the labels 1*, 2* and so on)
        val xAxis = skillRatingChart.getXAxis()
        xAxis.setDrawGridLines(false)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setEnabled(true)
        xAxis.setDrawAxisLine(false)


        val yLeft = skillRatingChart.axisLeft
        val usageResults = AppDatabase.getInstance(this)?.micUsedDao()?.getAll()

//Set the minimum and maximum bar lengths as per the values that they represent
        yLeft.axisMaximum = usageResults?.maxBy{ res -> res.count}?.count?.toFloat() ?: 100f
        yLeft.axisMinimum = 0f
        yLeft.isEnabled = false




        val yRight = skillRatingChart.axisRight
        yRight.setDrawAxisLine(true)
        yRight.setDrawGridLines(false)
        yRight.isEnabled = false


        val distinctAppNames = usageResults?.map { r -> r.fenceName }?.distinct()


        //Set label count to 5 as we are displaying 5 star rating
        if (distinctAppNames != null) {
            xAxis.setLabelCount(distinctAppNames.count().toInt())
        }

//Now add the labels to be added on the vertical axis
        val values = distinctAppNames
        xAxis.valueFormatter = XAxisValueFormatter(values!!.toTypedArray())
        xAxis.textSize = 25f

        //Set bar entries and add necessary formatting
        setGraphData(usageResults, distinctAppNames.toTypedArray())

        //Add animation to the graph
        skillRatingChart.animateY(2000)
    }

    /**
     * Set the bar entries i.e. the percentage of users who rated the skill with
     * a certain number of stars.
     *
     * Set the colors for different bars and the bar width of the bars.
     */
    private fun setGraphData(
        usageResults: List<MicrophoneIsBeingUsed>?,
        distinctFenceNames: Array<String>
    ) {

        //Add a list of bar entries
        val entries = ArrayList<BarEntry>()
        if (usageResults != null) {
            for (i in (distinctFenceNames.indices)){
                entries.add(BarEntry(i.toFloat(), usageResults.filter({e-> e.fenceName.equals(distinctFenceNames[i])}).first().count.toFloat()))
            }
        }

        //Note : These entries can be replaced by real-time data, say, from an API

        val barDataSet = BarDataSet(entries, "Bar Data Set")

        barDataSet.setValueTextSize(20f)
   //     barDataSet.setColors(
        //        ContextCompat.getColor(skillRatingChart.context, R.color.Red),
         //   ContextCompat.getColor(skillRatingChart.context, R.color.Green),
          //  ContextCompat.getColor(skillRatingChart.context, R.color.Blue),
           // ContextCompat.getColor(skillRatingChart.context, R.color.Navy),
            //ContextCompat.getColor(skillRatingChart.context, R.color.Purple))

        //Set bar shadows
        skillRatingChart.setDrawBarShadow(true)
        barDataSet.barShadowColor = Color.argb(40, 150, 150, 150)
        val data = BarData(barDataSet)

        //Set the bar width
        //Note : To increase the spacing between the bars set the value of barWidth to < 1f
        data.barWidth = 0.9f

        //Finally set the data and refresh the graph
        skillRatingChart.data = data
        skillRatingChart.invalidate()
    }


}
