package com.ajstudios.easyattendance.realm

import io.realm.RealmList
import io.realm.RealmObject

open class Attendance_Reports : RealmObject() {
    var date: String? = null
    var monthOnly: String? = null
    var dateOnly: String? = null
    var classId: String? = null
    var date_and_classID: String? = null
    var classname: String? = null
    var subjName: String? = null
    var attendance_students_lists: RealmList<Attendance_Students_List>? = null
}