package com.example.weatherday

import io.realm.RealmObject

open class Analyze: RealmObject() {
    var yearMonthDay = ""
    var weekDay = ""
    var temperature = ""
    var daylight = ""
    var precipitation = ""
}