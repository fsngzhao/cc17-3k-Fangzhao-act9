package com.example.flightsearch

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightsearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var flightRepository: FlightRepository
    private lateinit var preferencesManager: PreferencesManager
    private val favoriteFlights = mutableListOf<Flight>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Flight Search"

        flightRepository = FlightRepository(this)
        preferencesManager = PreferencesManager(this)

        val searchText = binding.searchText
        val flightList = binding.recyclerView
        flightList.layoutManager = LinearLayoutManager(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, flightRepository.getAirportNames())
        searchText.setAdapter(adapter)

        searchText.setOnItemClickListener { parent, _, position, _ ->
            val selectedAirport = parent.getItemAtPosition(position).toString()
            val flights = flightRepository.getFlightsFrom(selectedAirport)
            flightList.adapter = FlightAdapter(flights) { flight ->
                if (!favoriteFlights.contains(flight)) {
                    favoriteFlights.add(flight)
                    preferencesManager.addFavorite(flight)
                } else {
                    favoriteFlights.remove(flight)
                    preferencesManager.clearFavorites()
                }
            }
        }
    }
}


