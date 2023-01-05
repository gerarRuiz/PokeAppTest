package com.ruizdev.pokeapptest.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.LocationItemBinding
import com.ruizdev.pokeapptest.domain.model.LocationEvent

class LocationAdapter(
    private val locationList: MutableList<LocationEvent>,
    private var callback: (item: LocationEvent?) -> Unit
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LocationItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locationList[position])
    }

    override fun getItemCount(): Int = locationList.size

    fun add(locationEvent: LocationEvent) {
        if (!locationList.contains(locationEvent)) {
            locationList.add(locationEvent)
            notifyItemInserted(locationList.size - 1)
        } else {
            update(locationEvent)
        }
    }

    fun update(locationEvent: LocationEvent) {
        val index = locationList.indexOf(locationEvent)
        if (index != -1) {
            locationList[index] = locationEvent
            notifyItemChanged(index)
        }
    }

    fun delete(locationEvent: LocationEvent) {
        val index = locationList.indexOf(locationEvent)
        if (index != -1) {
            locationList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class ViewHolder(
        private val binding: LocationItemBinding,
        private val callback: (item: LocationEvent?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var context = binding.root.context

        fun bind(locationEvent: LocationEvent) {

            with(binding) {

                tvLatitud.text = context.getString(R.string.tv_info_latitud, locationEvent.latitude.toDouble())
                tvLongitud.text = context.getString(R.string.tv_info_longitud, locationEvent.longitude.toDouble())
                tvAddress.text = locationEvent.address

                btnContinueToMap.setOnClickListener { callback.invoke(locationEvent) }

            }

        }

    }

}