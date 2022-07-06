package com.giniapps.currentweather.screens.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giniapps.currentweather.data.repository.models.LocationModel
import com.giniapps.currentweather.databinding.LocationItemBinding

class LocationListAdapter(
    private val onDeleteButtonClicked: (LocationModel) -> Unit
): RecyclerView.Adapter<LocationListAdapter.LocationViewHolder>() {
    var items = emptyList<LocationModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LocationItemBinding.inflate(inflater)
        return LocationViewHolder(binding, onDeleteButtonClicked)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    class LocationViewHolder(
        private val binding: LocationItemBinding,
        private val onDeleteButtonClicked: (LocationModel) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocationModel) {

            with(binding) {
                root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                countryName.text = item.countryName
                removeButton.setOnClickListener {
                    onDeleteButtonClicked(item)
                }
            }
        }
    }
}