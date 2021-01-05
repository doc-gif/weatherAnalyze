package com.example.weatherday

import io.realm.RealmObject

open class Data: RealmObject() {
    var yearMonthDay = ""
    var weekDay = ""
    var temperature = ""
    var daylight = ""
    var precipitation = ""
}