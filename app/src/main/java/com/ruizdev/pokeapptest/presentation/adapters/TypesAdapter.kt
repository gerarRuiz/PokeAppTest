package com.ruizdev.pokeapptest.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ruizdev.pokeapptest.databinding.ItemTypeBinding
import com.ruizdev.pokeapptest.domain.model.Types

class TypesAdapter: ListAdapter<Types, TypesAdapter.ViewHolder>(
    DiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val item = getItem(position)
            holder.bind(item)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    class ViewHolder(
        private val binding: ItemTypeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(type: Types?) {
            binding.tvTypeName.text = type?.type?.name?.replaceFirstChar(Char::titlecase)
        }

    }


}

class DiffUtilCallback : DiffUtil.ItemCallback<Types>() {
    override fun areItemsTheSame(oldItem: Types, newItem: Types): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: Types, newItem: Types): Boolean {
        return oldItem == newItem
    }
}