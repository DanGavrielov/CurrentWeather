package com.giniapps.currentweather.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel
import com.giniapps.currentweather.databinding.WeatherItemBinding
import com.squareup.picasso.Picasso

class TrackedLocationsAdapter(
    private val onItemClicked: (WeatherDetailsModel) -> Unit
): RecyclerView.Adapter<TrackedLocationsAdapter.TrackedLocationViewHolder>() {
    var items = emptyList<WeatherDetailsModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackedLocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WeatherItemBinding.inflate(inflater)
        return TrackedLocationViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: TrackedLocationViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    class TrackedLocationViewHolder(
        private val binding: WeatherItemBinding,
        private val onItemClicked: (WeatherDetailsModel) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherDetailsModel) {
            with(binding) {
                root.setOnClickListener { onItemClicked(item) }
                countryName.text = item.countryName
                if (item.iconUrl.isNotEmpty()) {
                    Picasso.get().load(item.iconUrl).into(weatherIcon)
                }
            }
        }
    }
}