package com.ajstudios.easyattendance.BottomSheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.ajstudios.easyattendance.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Student_Edit_Sheet(
    private var _name: String?,
    private var _regNo: String?,
    private var _mobNo: String?
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false)
        val name_student = v.findViewById<EditText>(R.id.stu_name_edit)
        val regNo_student = v.findViewById<EditText>(R.id.stu_regNo_edit)
        val mobNo_student = v.findViewById<EditText>(R.id.stu_mobNo_edit)
        val call = v.findViewById<EditText>(R.id.call_edit)
        name_student.setText(_name)
        regNo_student.setText(_regNo)
        mobNo_student.setText(_mobNo)
        call.setOnClickListener {
            val uri = "tel:" + _mobNo?.trim { it <= ' ' }
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
        return v
    }
}