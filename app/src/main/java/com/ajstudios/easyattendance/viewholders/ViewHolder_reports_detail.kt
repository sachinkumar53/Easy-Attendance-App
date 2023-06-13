package com.ajstudios.easyattendance.viewholders

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.realm.Attendance_Students_List
import io.realm.RealmResults

class ViewHolder_reports_detail(
    itemView: View,
    MainActivity: Activity,
    list: RealmResults<Attendance_Students_List>
) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var namE: TextView
    @JvmField
    var regNo: TextView
    @JvmField
    var status: TextView
    @JvmField
    var circle: CardView
    var mActivity: Activity
    var mList: RealmResults<Attendance_Students_List>

    init {
        namE = itemView.findViewById(R.id.student_name_report_detail_adapter)
        regNo = itemView.findViewById(R.id.student_regNo_report_detail_adapter)
        status = itemView.findViewById(R.id.status_report_detail_adapter)
        circle = itemView.findViewById(R.id.cardView_report_detail_adapter)
        mActivity = MainActivity
        mList = list
    }
}