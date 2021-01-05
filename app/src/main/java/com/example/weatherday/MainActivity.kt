package com.example.weatherday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weatherday.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.apache.commons.csv.CSVFormat
import java.io.*

class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var binding: ActivityMainBinding
    private var analyzeResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        realm = Realm.getDefaultInstance()

        analyze("1/")
        analyze("2/")
        analyze("3/")
        analyze("4/")
        analyze("5/")
        analyze("6/")
        analyze("7/")
        analyze("8/")
        analyze("9/")
        analyze("10")
        analyze("11")
        analyze("12")

    }

    private fun createData() {
        val reader = BufferedReader(InputStreamReader(resources.assets.open("weatherDataDay.csv")))
        reader.use {
            val records = CSVFormat.EXCEL.parse(reader)
            realm.executeTransaction {
                val target = realm.where<Data>().findAll()
                    target.deleteAllFromRealm()
                records.records.forEach { record ->
                    val obj = realm.createObject<Data>()
                    obj.yearMonthDay = record[0]
                    obj.weekDay = record[1]
                    obj.temperature = record[2]
                    obj.daylight = record[3]
                    obj.precipitation = record[4]
                }
            }
        }
    }

    private fun analyze(x: String) {
        val realmResult = realm.where<Data>().findAll()
        realm.executeTransaction {
            val target = realm.where<Analyze>().findAll()
            target.deleteAllFromRealm()
            for (i in 0 until realmResult.size - 1) {
                val a =  realmResult[i]?.yearMonthDay.toString().take(7).takeLast(2)
                /**
                 * 雨　　realmResult[i]?.precipitation?.toDouble() != 0.0
                 * 晴　　realmResult[i]?.daylight!!.toDouble() >= 2.0 && realmResult[i]?.precipitation?.toDouble() == 0.0
                 * 曇り　realmResult[i]?.daylight!!.toDouble() < 2.0 && realmResult[i]?.precipitation?.toDouble() == 0.0
                 * 雨　　realmResult[i + 1]?.precipitation?.toDouble() != 0.0
                 * 晴　　realmResult[i + 1]?.daylight!!.toDouble() >= 2.0 && realmResult[i + 1]?.precipitation?.toDouble() == 0.0
                 * 曇り　realmResult[i + 1]?.daylight!!.toDouble() < 2.0 && realmResult[i + 1]?.precipitation?.toDouble() == 0.0
                 */
                if (a == x) {
                    if ((realmResult[i]?.daylight!!.toDouble() >= 2.0 && realmResult[i]?.precipitation?.toDouble() == 0.0) && (realmResult[i + 1]?.daylight!!.toDouble() < 2.0 && realmResult[i + 1]?.precipitation?.toDouble() == 0.0)) {
                        val realmObject1 = realm.createObject<Analyze>()
                        realmObject1.yearMonthDay = realmResult[i]?.yearMonthDay.toString()
                    }
                }
            }
        }
        put()
    }

    private fun put() {
        val realmResult = realm.where<Analyze>().findAll()
        analyzeResult = ""
        for (i in 0 until realmResult.size) {
            analyzeResult += "${realm.where<Analyze>().findAll()[i]?.yearMonthDay},"
        }
        Log.e("result","$analyzeResult")
        Log.e("count", "${realmResult.size}")
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}