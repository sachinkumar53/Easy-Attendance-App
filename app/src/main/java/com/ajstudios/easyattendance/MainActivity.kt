package com.ajstudios.easyattendance

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ajstudios.easyattendance.Adapter.ClassListAdapter
import com.ajstudios.easyattendance.realm.Class_Names
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm

class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        window.enterTransition = null

        val fabMain = findViewById<FloatingActionButton>(R.id.fab_main)

        fabMain.setOnClickListener {
            val intent = Intent(this@MainActivity, InsertClassActivity::class.java)
            startActivity(intent)
        }

        realm = Realm.getDefaultInstance()
        val results = realm.where(Class_Names::class.java).findAll()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_main)
        recyclerView.apply {
            setHasFixedSize(true)
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            recyclerView.adapter = ClassListAdapter(results, this@MainActivity)
        }
    }

    override fun onResume() {
        realm.refresh()
        realm.isAutoRefresh = true
        super.onResume()
    }
}