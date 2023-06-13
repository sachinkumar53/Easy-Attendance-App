package com.ajstudios.easyattendance.viewholders

import android.app.Activity
import android.preference.PreferenceManager
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ajstudios.easyattendance.BottomSheet.Student_Edit_Sheet
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.realm.Attendance_Reports
import com.ajstudios.easyattendance.realm.Attendance_Students_List
import com.ajstudios.easyattendance.realm.Students_List
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class ViewHolder_students(
    itemView: View,
    MainActivity: Activity,
    list: RealmResults<Students_List>,
    roomID: String
) : RecyclerView.ViewHolder(itemView) {
    var mActivity: Activity
    var mList: RealmResults<Students_List>

    @JvmField
    val student_name: TextView

    @JvmField
    val student_regNo: TextView
    var layout: LinearLayout
    var stuName: String? = null
    var regNo: String? = null
    var mobileNo: String? = null
    var mRoomID: String
    var radioGroup: RadioGroup

    @JvmField
    var radioButton_present: RadioButton

    @JvmField
    var radioButton_absent: RadioButton
    var realm: Realm
    var realmChangeListener: RealmChangeListener<Realm>

    init {
        student_name = itemView.findViewById(R.id.student_name_adapter)
        student_regNo = itemView.findViewById(R.id.student_regNo_adapter)
        radioGroup = itemView.findViewById(R.id.radioGroup)
        radioButton_present = itemView.findViewById(R.id.radio_present)
        radioButton_absent = itemView.findViewById(R.id.radio_absent)
        layout = itemView.findViewById(R.id.layout_click)
        mActivity = MainActivity
        mList = list
        mRoomID = roomID

        realm = Realm.getDefaultInstance()
        realmChangeListener = RealmChangeListener {
            val reports_size = realm.where(Attendance_Reports::class.java)
                .equalTo("date_and_classID", roomID)
                .count()
            if (reports_size != 0L) {
                radioGroup.visibility = View.GONE
            } else if (reports_size == 0L) {
                radioGroup.visibility = View.VISIBLE
            }
        }
        realm.addChangeListener(realmChangeListener)
        val reports_size = realm.where(Attendance_Reports::class.java)
            .equalTo("date_and_classID", roomID)
            .count()
        if (reports_size != 0L) {
            radioGroup.visibility = View.GONE
        } else if (reports_size == 0L) {
            radioGroup.visibility = View.VISIBLE
        }
        radioButton_present.setOnClickListener {
            val attendance = "Present"
            val preferences = PreferenceManager.getDefaultSharedPreferences(mActivity)
            val editor = preferences.edit()
            editor.putString(mList[absoluteAdapterPosition]!!.regNo_student, attendance)
            editor.apply()
            val attendance_students_list = Attendance_Students_List()
            realm.executeTransaction { realm ->
                attendance_students_list.studentName = mList[absoluteAdapterPosition]!!
                    .name_student
                attendance_students_list.attendance = attendance
                attendance_students_list.mobNo = mList[absoluteAdapterPosition]!!
                    .mobileNo_student
                attendance_students_list.studentRegNo =
                    mList[absoluteAdapterPosition]!!.regNo_student
                attendance_students_list.classID = mList[absoluteAdapterPosition]!!.class_id
                attendance_students_list.date_and_classID = mRoomID
                attendance_students_list.unique_ID =
                    mList[absoluteAdapterPosition]!!.regNo_student + mRoomID
                realm.copyToRealmOrUpdate(attendance_students_list)
            }
        }
        radioButton_absent.setOnClickListener {
            val attendance = "Absent"
            val preferences = PreferenceManager.getDefaultSharedPreferences(mActivity)
            val editor = preferences.edit()
            editor.putString(mList[absoluteAdapterPosition]!!.regNo_student, attendance)
            editor.apply()
            val attendance_students_list = Attendance_Students_List()
            realm.executeTransaction { realm ->
                attendance_students_list.studentName = mList[absoluteAdapterPosition]!!
                    .name_student
                attendance_students_list.attendance = attendance
                attendance_students_list.mobNo = mList[absoluteAdapterPosition]!!
                    .mobileNo_student
                attendance_students_list.studentRegNo =
                    mList[absoluteAdapterPosition]!!.regNo_student
                attendance_students_list.classID = mList[absoluteAdapterPosition]!!.class_id
                attendance_students_list.date_and_classID = mRoomID
                attendance_students_list.unique_ID =
                    mList[absoluteAdapterPosition]!!.regNo_student + mRoomID
                realm.copyToRealmOrUpdate(attendance_students_list)
            }
        }
        layout.setOnClickListener { view ->
            stuName = mList[absoluteAdapterPosition]?.name_student
            regNo = mList[absoluteAdapterPosition]?.regNo_student
            mobileNo = mList[absoluteAdapterPosition]?.mobileNo_student
            val student_edit_sheet = Student_Edit_Sheet(stuName, regNo, mobileNo)
            student_edit_sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
            student_edit_sheet.show(
                (view.context as FragmentActivity).supportFragmentManager,
                "BottomSheet"
            )
        }
    }
}