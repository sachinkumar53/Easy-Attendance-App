package com.ajstudios.easyattendance.viewholders

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.ReportsDetailActivity
import com.ajstudios.easyattendance.realm.Attendance_Reports
import io.realm.RealmResults

class ViewHolder_reports(
    itemView: View,
    private val activity: Activity,
    private val list: RealmResults<Attendance_Reports>
) : RecyclerView.ViewHolder(itemView) {

    val month: TextView
    val date: TextView

    init {
        month = itemView.findViewById(R.id.month_report_adapter)
        date = itemView.findViewById(R.id.date_report_adapter)
        itemView.setOnClickListener { view ->
            val intent = Intent(view.context, ReportsDetailActivity::class.java)
            intent.putExtra("ID", list[absoluteAdapterPosition]!!.date_and_classID)
            intent.putExtra("date", list[absoluteAdapterPosition]!!.date)
            intent.putExtra("subject", list[absoluteAdapterPosition]!!.subjName)
            intent.putExtra("class", list[absoluteAdapterPosition]!!.classname)
            view.context.startActivity(intent)
        }
    }
}