package com.ajstudios.easyattendance.realm

import io.realm.RealmObject

open class Students_List : RealmObject() {
    var id: String? = null
    var name_student: String? = null
    var regNo_student: String? = null
    var mobileNo_student: String? = null
    var class_id: String? = null
}