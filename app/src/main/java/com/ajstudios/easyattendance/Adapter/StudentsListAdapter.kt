package com.ajstudios.easyattendance.Adapter

import android.app.Activity
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.realm.Students_List
import com.ajstudios.easyattendance.viewholders.ViewHolder_students
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class StudentsListAdapter(
    var mList: RealmResults<Students_List>,
    private val mActivity: Activity,
    var mroomID: String,
//    extraClick: String?
) : RealmRecyclerViewAdapter<Students_List?, ViewHolder_students>(
    mActivity, mList, true
) {
    var stuID: String? = null

    //    var realm = Realm.getDefaultInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_students {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_attendance_adapter, parent, false)
        return ViewHolder_students(itemView, mActivity, mList, mroomID)
    }

    override fun onBindViewHolder(holder: ViewHolder_students, position: Int) {
        val temp = getItem(position)
        holder.student_name.text = temp?.name_student
        holder.student_regNo.text = temp?.regNo_student

        val preferences = PreferenceManager.getDefaultSharedPreferences(mActivity)
        stuID = temp?.regNo_student
        val value = preferences.getString(stuID, null)
        if (value != null) {
            if (value == "Present") {
                holder.radioButton_present.isChecked = true
            } else {
                holder.radioButton_absent.isChecked = true
            }
        }
    }
}