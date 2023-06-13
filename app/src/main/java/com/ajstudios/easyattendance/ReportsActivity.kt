package com.ajstudios.easyattendance

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajstudios.easyattendance.Adapter.ReportsAdapter
import com.ajstudios.easyattendance.realm.Attendance_Reports
import io.realm.Realm

class ReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        Realm.init(this)

        val subjectName = intent.getStringExtra("subject_name")
        val className = intent.getStringExtra("class_name")
        val roomId = intent.getStringExtra("room_ID")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_reports)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_reports)
        setSupportActionBar(toolbar)
        toolbar.apply {
            title = subjectName
            subtitle = className
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val realm = Realm.getDefaultInstance()
        val results = realm.where(Attendance_Reports::class.java)
            .equalTo("classId", roomId)
            .findAll()

        recyclerView.apply {
            setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this@ReportsActivity, 4)
            recyclerView.adapter = ReportsAdapter(results, this@ReportsActivity)
        }
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