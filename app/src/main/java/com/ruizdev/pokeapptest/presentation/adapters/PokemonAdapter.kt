package com.ruizdev.pokeapptest.presentation.adapters

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.ItemPokemonBinding
import com.ruizdev.pokeapptest.domain.model.Pokemon
import com.ruizdev.pokeapptest.util.Constants

class PokemonAdapter(
    var textColor: Int,
    var backgroundColor: Int,
    var callback: (item: Pokemon?) -> Unit,
    var callbackFavorite: (item: Pokemon?) -> Unit,
    var callbackImage: (id: Int?) -> Unit
) : PagingDataAdapter<Pokemon, PokemonAdapter.ViewHolder>(
    DiffUtilCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            textColor,
            backgroundColor,
            binding,
            callback,
            callbackFavorite,
            callbackImage
        )
    }

    class ViewHolder(
        var textColor: Int,
        var backgroundColor: Int,
        private val binding: ItemPokemonBinding,
        private val callback: (item: Pokemon?) -> Unit,
        private val callbackFavorite: (item: Pokemon?) -> Unit,
        private val callbackImage: (id: Int?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(pokemon: Pokemon?) {

            binding.tvPokemonName.text = pokemon?.name?.replaceFirstChar(Char::titlecase)
            binding.tvPokemonNumber.text =
                binding.root.context.getString(R.string.pokemon_number, pokemon?.id?.toString())
            if (pokemon?.favorite == true) binding.imageViewFavorite.setImageResource(R.drawable.ic_favorite) else binding.imageViewFavorite.setImageResource(
                R.drawable.ic_no_favorite
            )

            binding.imageViewSprite.backgroundTintList = ColorStateList.valueOf(backgroundColor)
            binding.tvPokemonInitials.setTextColor(textColor)

            loadImage(pokemon)

            binding.constraintRoot.setOnClickListener { callback(pokemon) }
            binding.imageViewFavorite.setOnClickListener { callbackFavorite(pokemon) }
            binding.imageViewSprite.setOnClickListener { callbackImage(pokemon?.id) }

        }

        private fun loadImage(pokemon: Pokemon?) {
            Glide.with(binding.imageViewSprite)
                .load("${Constants.BASE_URL_SPRITE}${pokemon?.id}.png")
                .apply(RequestOptions.circleCropTransform())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val splitted = pokemon?.name?.split("\\s".toRegex())
                        if (splitted.isNullOrEmpty()) {
                            binding.imageViewSprite.background = ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.ic_image,
                                null
                            )
                        } else {

                            binding.tvPokemonInitials.visibility = View.VISIBLE

                            if (splitted.size == 1) {
                                binding.tvPokemonInitials.text =
                                    splitted[0].first().uppercaseChar().toString()
                            } else if (splitted.size > 1) {
                                binding.tvPokemonInitials.text =
                                    "${splitted[0].first().uppercaseChar()}${
                                        splitted[1].first().uppercaseChar()
                                    }"
                            }

                        }

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        binding.imageViewSprite.background =
                            ResourcesCompat.getDrawable(context.resources, R.drawable.circle, null)
                        binding.tvPokemonInitials.visibility = View.GONE
                        return false
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageViewSprite)
        }

    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }


}