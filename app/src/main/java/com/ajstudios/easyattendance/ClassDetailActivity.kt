package com.ajstudios.easyattendance

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajstudios.easyattendance.Adapter.StudentsListAdapter
import com.ajstudios.easyattendance.realm.Attendance_Reports
import com.ajstudios.easyattendance.realm.Attendance_Students_List
import com.ajstudios.easyattendance.realm.Students_List
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.yarolegovich.lovelydialog.LovelyCustomDialog
import io.realm.Realm
import io.realm.RealmAsyncTask
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ClassDetailActivity : AppCompatActivity() {
    private lateinit var totalStudents: TextView
    private lateinit var layoutAttendanceTaken: LinearLayout
    private lateinit var submitBtn: Button
    private lateinit var placeHolder: TextView
    private lateinit var mRecyclerview: RecyclerView
    private lateinit var realm: Realm
    private lateinit var studentName: EditText
    private lateinit var regNo: EditText
    private lateinit var mobileNo: EditText

    private var roomId: String? = null
    private var subjectName: String? = null
    private var className1: String? = null
    private var transaction: RealmAsyncTask? = null
    private var lovelyCustomDialog: Dialog? = null

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_detail_)
        window.exitTransition = null
        Realm.init(this)

        val theme = intent.getStringExtra("theme")
        className1 = intent.getStringExtra("className")
        subjectName = intent.getStringExtra("subjectName")
        roomId = intent.getStringExtra("classroom_ID")
        val toolbar = findViewById<Toolbar>(R.id.toolbar_class_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val collapsingToolbarLayout =
            findViewById<CollapsingToolbarLayout>(R.id.collapsing_disease_detail)
        collapsingToolbarLayout.title = subjectName

        val themeImage = findViewById<ImageView>(R.id.image_disease_detail)
        val className = findViewById<TextView>(R.id.classname_detail)
        totalStudents = findViewById(R.id.total_students_detail)

        layoutAttendanceTaken = findViewById(R.id.attendance_taken_layout)
        layoutAttendanceTaken.visibility = View.GONE

        val addStudent = findViewById<CardView>(R.id.add_students)
        val reportsOpen = findViewById<CardView>(R.id.reports_open_btn)
        className.text = className1

        mRecyclerview = findViewById(R.id.recyclerView_detail)
        val progressBar = findViewById<ProgressBar>(R.id.progressbar_detail)

        placeHolder = findViewById(R.id.placeholder_detail)
        placeHolder.visibility = View.GONE

        submitBtn = findViewById(R.id.submit_attendance_btn)
        submitBtn.visibility = View.GONE

        when (theme) {
            "0" -> themeImage.setImageResource(R.drawable.asset_bg_paleblue)
            "1" -> themeImage.setImageResource(R.drawable.asset_bg_green)
            "2" -> themeImage.setImageResource(R.drawable.asset_bg_yellow)
            "3" -> themeImage.setImageResource(R.drawable.asset_bg_palegreen)
            "4" -> themeImage.setImageResource(R.drawable.asset_bg_paleorange)
            "5" -> themeImage.setImageResource(R.drawable.asset_bg_white)
        }

        //---------------------------------
        val r = Runnable {
            realmInit()
            progressBar.visibility = View.GONE
        }

        val handler = Handler()
        handler.postDelayed(r, 500)

        //----------------------------------------
        submitBtn.setOnClickListener {
            val count = realm.where(Students_List::class.java)
                .equalTo("class_id", roomId)
                .count()

            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val size = preferences.all.size.toString()
            val size2 = count.toString()
            if (size == size2) {
                submitAttendance()
            } else {
                Toast.makeText(this@ClassDetailActivity, "Select all........", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        reportsOpen.setOnClickListener {
            val intent = Intent(this@ClassDetailActivity, ReportsActivity::class.java)
            intent.putExtra("class_name", className1)
            intent.putExtra("subject_name", subjectName)
            intent.putExtra("room_ID", roomId)
            startActivity(intent)
        }

        addStudent.setOnClickListener {
            val inflater = LayoutInflater.from(this@ClassDetailActivity)
            val view1 = inflater.inflate(R.layout.popup_add_student, null)
            studentName = view1.findViewById(R.id.name_student_popup)
            regNo = view1.findViewById<EditText>(R.id.regNo_student_popup)
            mobileNo = view1.findViewById<EditText>(R.id.mobileNo_student_popup)

            lovelyCustomDialog = LovelyCustomDialog(this@ClassDetailActivity)
                .setView(view1)
                .setTopColorRes(R.color.theme_light)
                .setTitle("Add Student")
                .setIcon(R.drawable.ic_baseline_person_add_24)
                .setCancelable(false)
                .setListener(R.id.add_btn_popup) {
                    val name = studentName.text.toString()
                    val regNo = regNo.text.toString()
                    val mobNo = mobileNo.text.toString()
                    if (isValid) {
                        addStudentMethod(name, regNo, mobNo)
                    } else {
                        Toast.makeText(
                            this,
                            "Please fill all the details..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .setListener(R.id.cancel_btn_popup) { lovelyCustomDialog!!.dismiss() }
                .show()
        }
    }

    private fun realmInit() {
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        val date = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Date())
        val realmChangeListener = RealmChangeListener<Realm> {
            val count = realm.where(Students_List::class.java)
                .equalTo("class_id", roomId)
                .count()

            totalStudents.text = "Total Students : $count"

            val reportsSize = realm.where(Attendance_Reports::class.java)
                .equalTo("date_and_classID", date + roomId)
                .count()

            if (reportsSize != 0L) {
                layoutAttendanceTaken.visibility = View.VISIBLE
                submitBtn.visibility = View.GONE
            } else {
                layoutAttendanceTaken.visibility = View.GONE
                submitBtn.visibility = View.VISIBLE
                if (count != 0L) {
                    submitBtn.visibility = View.VISIBLE
                    placeHolder.visibility = View.GONE
                } else {
                    submitBtn.visibility = View.GONE
                    placeHolder.visibility = View.VISIBLE
                }
            }
        }

        realm.addChangeListener(realmChangeListener)

        val students = realm.where(Students_List::class.java)
            .equalTo("class_id", roomId)
            .sort("name_student", Sort.ASCENDING)
            .findAllAsync()

        val count = realm.where(Students_List::class.java)
            .equalTo("class_id", roomId)
            .count()

        val reportsSize = realm.where(Attendance_Reports::class.java)
            .equalTo("date_and_classID", date + roomId)
            .count()

        if (reportsSize != 0L) {
            layoutAttendanceTaken.visibility = View.VISIBLE
            submitBtn.visibility = View.GONE
        } else {
            layoutAttendanceTaken.visibility = View.GONE
            submitBtn.visibility = View.VISIBLE

            if (count != 0L) {
                submitBtn.visibility = View.VISIBLE
                placeHolder.visibility = View.GONE
            } else {
                submitBtn.visibility = View.GONE
                placeHolder.visibility = View.VISIBLE
            }
        }

        totalStudents.text = "Total Students : $count"
        mRecyclerview.layoutManager = LinearLayoutManager(this)


        val studentsListAdapter = StudentsListAdapter(students, this, date + roomId)
        mRecyclerview.adapter = studentsListAdapter
    }

    private fun submitAttendance() {
        val progressDialog = ProgressDialog(this@ClassDetailActivity)
        progressDialog.setMessage("Please wait..")
        progressDialog.show()
        val date = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Date())
        val listStudents = realm.where(Attendance_Students_List::class.java)
            .equalTo("date_and_classID", date + roomId)
            .sort("studentName", Sort.ASCENDING)
            .findAllAsync()

        val list = RealmList<Attendance_Students_List>()
        list.addAll(listStudents)

        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val dateOnly = calendar[Calendar.DATE].toString()

        val monthOnly = SimpleDateFormat("MMM").format(calendar.time)
        try {
            realm.executeTransaction { realm ->
                val attendanceReports = realm.createObject(Attendance_Reports::class.java)
                attendanceReports.classId = roomId
                attendanceReports.attendance_students_lists = list
                attendanceReports.date = date
                attendanceReports.dateOnly = dateOnly
                attendanceReports.monthOnly = monthOnly
                attendanceReports.date_and_classID = date + roomId
                attendanceReports.classname = className1
                attendanceReports.subjName = subjectName
            }

            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.clear()
            editor.commit()
            Toast.makeText(this@ClassDetailActivity, "Attendance Submitted", Toast.LENGTH_SHORT)
                .show()
            progressDialog.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
            Toast.makeText(this@ClassDetailActivity, "Error Occurred", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
        super.onDestroy()
    }

    private fun addStudentMethod(studentName: String, regNo: String, mobileNo: String?) {
        val progressDialog = ProgressDialog(this@ClassDetailActivity)
        progressDialog.setMessage("Creating class..")
        progressDialog.show()
        transaction = realm.executeTransactionAsync({ realm ->
            val studentsList = realm.createObject(Students_List::class.java)
            val id = studentName + regNo
            studentsList.id = id
            studentsList.name_student = studentName
            studentsList.regNo_student = regNo
            studentsList.mobileNo_student = mobileNo
            studentsList.class_id = roomId
        }, {
            progressDialog.dismiss()
            lovelyCustomDialog?.dismiss()
            realm.refresh()
            realm.isAutoRefresh = true
            Toast.makeText(this@ClassDetailActivity, "Student Added", Toast.LENGTH_SHORT).show()
        }) {
            progressDialog.dismiss()
            lovelyCustomDialog?.dismiss()
            Toast.makeText(this@ClassDetailActivity, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    val isValid: Boolean
        get() = !(studentName!!.text.toString().isEmpty() || regNo!!.text.toString()
            .isEmpty() || mobileNo!!.text.toString().isEmpty())

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_class_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /*companion object {
        const val TAG = "ClassDetail_Activity"
    }*/
}