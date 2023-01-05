package com.ruizdev.pokeapptest.presentation.fragments.image

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.FragmentImageBinding
import com.ruizdev.pokeapptest.presentation.common.BaseFragment
import com.ruizdev.pokeapptest.util.Constants

class ImageFragment : BaseFragment<FragmentImageBinding>(FragmentImageBinding::inflate) {

    private lateinit var binding: FragmentImageBinding

    private val safeArgs: ImageFragmentArgs by navArgs()

    override fun FragmentImageBinding.initialize() {
        binding = this

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        Glide.with(requireContext())
            .load("${Constants.BASE_URL_SPRITE}${safeArgs.idPokemon}.png")
            .centerCrop()
            .placeholder(R.drawable.ic_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imageViewSpriteFull)

    }


}