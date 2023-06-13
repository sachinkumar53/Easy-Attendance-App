package com.ajstudios.easyattendance.realm

import io.realm.RealmObject

open class Class_Names : RealmObject() {
    var id: String? = null
    var name_class: String? = null
    var name_subject: String? = null
    var position_bg: String? = null
}