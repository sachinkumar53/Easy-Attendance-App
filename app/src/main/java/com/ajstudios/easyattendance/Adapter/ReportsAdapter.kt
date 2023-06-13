package com.ajstudios.easyattendance.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.realm.Attendance_Reports
import com.ajstudios.easyattendance.viewholders.ViewHolder_reports
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class ReportsAdapter(
    var mList: RealmResults<Attendance_Reports>,
    private val mActivity: Activity,
//    var mroomID: String
) : RealmRecyclerViewAdapter<Attendance_Reports?, ViewHolder_reports>(
    mActivity, mList, true
) {
    //    var stuID: String? = null
//    var realm = Realm.getDefaultInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_reports {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reports_adapter_item, parent, false)
        return ViewHolder_reports(itemView, mActivity, mList)
    }

    override fun onBindViewHolder(holder: ViewHolder_reports, position: Int) {
        val temp = getItem(position)
        holder.month.text = temp?.monthOnly
        holder.date.text = temp?.dateOnly
    }
}