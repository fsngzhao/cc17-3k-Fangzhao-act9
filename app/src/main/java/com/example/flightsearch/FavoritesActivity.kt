package com.example.flightsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightsearch.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)

        // Initialize RecyclerView
        val favoritesList = binding.recyclerView
        favoritesList.layoutManager = LinearLayoutManager(this)

        val favorites = preferencesManager.getFavorites()
        val adapter = FlightAdapter(favorites) { flight ->
        }
        favoritesList.adapter = adapter


        binding.clearAllButton.setOnClickListener {
            preferencesManager.clearFavorites()
            adapter.updateFlights(emptyList())
        }
    }
}
