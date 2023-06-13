package com.ajstudios.easyattendance.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.realm.Attendance_Students_List
import com.ajstudios.easyattendance.viewholders.ViewHolder_reports_detail
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class Reports_Detail_Adapter(
    var mList: RealmResults<Attendance_Students_List>,
    private val mActivity: Activity,
    //var mroomID: String
) : RealmRecyclerViewAdapter<Attendance_Students_List?, ViewHolder_reports_detail>(
    mActivity, mList, true
) {
    /*var stuID: String? = null
    private var realm = Realm.getDefaultInstance()*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_reports_detail {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.report_detail_adapter_item, parent, false)
        return ViewHolder_reports_detail(itemView, mActivity, mList)
    }

    override fun onBindViewHolder(holder: ViewHolder_reports_detail, position: Int) {
        val temp = getItem(position)
        holder.namE.text = temp?.studentName
        holder.regNo.text = temp?.studentRegNo

        if (temp?.attendance == "Present") {
            holder.status.text = "P"
            holder.circle.setCardBackgroundColor(mActivity.resources.getColor(R.color.green_new))
        } else {
            holder.status.text = "A"
            holder.circle.setCardBackgroundColor(mActivity.resources.getColor(R.color.red_new))
        }
    }
}