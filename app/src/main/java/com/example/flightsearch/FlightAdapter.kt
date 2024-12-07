package com.example.flightsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightsearch.databinding.FlightItemBinding

class FlightAdapter(
    private var flights: List<Flight>,
    private val onFavoriteClick: (Flight) -> Unit
) : RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val binding = FlightItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flights[position]
        holder.bind(flight)
    }

    override fun getItemCount(): Int = flights.size

    fun updateFlights(newFlights: List<Flight>) {
        flights = newFlights
        notifyDataSetChanged()
    }

    inner class FlightViewHolder(private val binding: FlightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flight: Flight) {
            binding.depart.text = "DEPART ${flight.departure} ${flight.departureName}"
            binding.arrive.text = "ARRIVE ${flight.arrival} ${flight.arrivalName}"

            val favoriteIcon = binding.favoriteIcon
            favoriteIcon.setImageResource(
                if (flight.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
            favoriteIcon.setOnClickListener {
                onFavoriteClick(flight)
                flight.isFavorite = !flight.isFavorite
                notifyItemChanged(adapterPosition)
            }
        }
    }
}
