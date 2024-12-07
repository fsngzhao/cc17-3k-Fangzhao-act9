package com.example.flightsearch

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream

class FlightRepository(context: Context) {
    private val databasePath: String = context.getDatabasePath("flight_search.db").path

    init {
        if (!File(databasePath).exists()) {
            copyDatabaseFromAssets(context)
        }
    }

    private fun copyDatabaseFromAssets(context: Context) {
        val inputStream = context.assets.open("flight_search.db")
        val outputStream = FileOutputStream(databasePath)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    private val database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY)

    fun getAirportNames(): List<String> {
        val cursor = database.rawQuery("SELECT DISTINCT iata_code FROM airport", null)
        val names = mutableListOf<String>()
        while (cursor.moveToNext()) {
            names.add(cursor.getString(0)) // iata_code 列
        }
        cursor.close()
        return names
    }

    fun getFlightsFrom(airport: String): List<Flight> {
        val cursor = database.rawQuery(
            "SELECT * FROM airport WHERE iata_code = ?", arrayOf(airport)
        )
        val flights = mutableListOf<Flight>()
        while (cursor.moveToNext()) {
            flights.add(
                Flight(
                    departure = cursor.getString(cursor.getColumnIndexOrThrow("iata_code")), // 出发机场代码
                    arrival = cursor.getString(cursor.getColumnIndexOrThrow("iata_code")), // 到达机场代码（假设相同）
                    departureName = cursor.getString(cursor.getColumnIndexOrThrow("name")), // 出发机场名称
                    arrivalName = cursor.getString(cursor.getColumnIndexOrThrow("name")) // 到达机场名称（假设相同）
                )
            )
        }
        cursor.close()
        return flights
    }
}
