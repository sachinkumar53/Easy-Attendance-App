package com.ajstudios.easyattendance.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Attendance_Students_List : RealmObject() {
    var studentName: String? = null
    var studentRegNo: String? = null
    var attendance: String? = null
    var mobNo: String? = null
    var classID: String? = null
    var date_and_classID: String? = null

    @PrimaryKey
    var unique_ID: String? = null
}