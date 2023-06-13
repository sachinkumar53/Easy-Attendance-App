package com.ajstudios.easyattendance

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajstudios.easyattendance.Adapter.Reports_Detail_Adapter
import com.ajstudios.easyattendance.realm.Attendance_Students_List
import io.realm.Realm
import io.realm.Sort

class ReportsDetailActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: Reports_Detail_Adapter
    private lateinit var subj: TextView
    private lateinit var className: TextView
    private lateinit var toolbar_title: TextView
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports__detail)
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        val roomId = intent.getStringExtra("ID")
        val classname = intent.getStringExtra("class")
        val subjName = intent.getStringExtra("subject")
        val date = intent.getStringExtra("date")
        val toolbar = findViewById<Toolbar>(R.id.toolbar_reports_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerView_reports_detail)
        subj = findViewById(R.id.subjName_report_detail)
        className = findViewById(R.id.classname_report_detail)
        toolbar_title = findViewById(R.id.toolbar_title)

        toolbar_title.text = date
        subj.text = subjName
        className.text = classname

        val list = realm.where(Attendance_Students_List::class.java)
            .equalTo("date_and_classID", roomId)
            .sort("studentName", Sort.ASCENDING)
            .findAllAsync()

        mAdapter = Reports_Detail_Adapter(list, this@ReportsDetailActivity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.only_dot, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}