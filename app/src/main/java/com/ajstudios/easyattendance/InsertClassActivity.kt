package com.ajstudios.easyattendance

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import co.ceryle.radiorealbutton.library.RadioRealButtonGroup
import com.ajstudios.easyattendance.realm.Class_Names
import io.realm.Realm
import io.realm.RealmAsyncTask

class InsertClassActivity : AppCompatActivity() {
    private lateinit var className: EditText
    private lateinit var subjectName: EditText
    private lateinit var realm: Realm
    private var transaction: RealmAsyncTask? = null
    private var positionBg = "0"

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_class_)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_insert_class)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val createButton = findViewById<Button>(R.id.button_createClass)
        className = findViewById(R.id.className_createClass)
        subjectName = findViewById(R.id.subjectName_createClass)

        Realm.init(this)
        realm = Realm.getDefaultInstance()
        val group = findViewById<View>(R.id.group) as RadioRealButtonGroup
        group.setOnClickedButtonPosition { position -> positionBg = position.toString() }
        createButton.setOnClickListener {
            if (isValid) {
                val progressDialog = ProgressDialog(this@InsertClassActivity)
                progressDialog.setMessage("Creating class..")
                progressDialog.show()
                transaction = realm.executeTransactionAsync(
                    { realm ->
                        val classNames = realm.createObject(Class_Names::class.java)
                        val id = className.text.toString() + subjectName.text.toString()
                        classNames.id = id
                        classNames.name_class = className.text.toString()
                        classNames.name_subject = subjectName.text.toString()
                        classNames.position_bg = positionBg
                    },
                    {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@InsertClassActivity,
                            "Successfully created",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    },
                    {
                        progressDialog.dismiss()
                        Toast.makeText(this@InsertClassActivity, "Error!", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            } else {
                Toast.makeText(this@InsertClassActivity, "Fill all details", Toast.LENGTH_SHORT)
                    .show()
            }

            //-------
        }
    }

    private val isValid: Boolean
        get() = className.text.toString().isNotEmpty() && subjectName.text.toString().isNotEmpty()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}