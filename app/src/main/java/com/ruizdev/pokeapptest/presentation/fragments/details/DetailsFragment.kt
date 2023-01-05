package com.ruizdev.pokeapptest.presentation.fragments.details

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.FragmentDetailsBinding
import com.ruizdev.pokeapptest.domain.model.ApiResponsePokemonDetail
import com.ruizdev.pokeapptest.domain.model.Types
import com.ruizdev.pokeapptest.presentation.adapters.TypesAdapter
import com.ruizdev.pokeapptest.presentation.common.BaseFragment
import com.ruizdev.pokeapptest.presentation.common.CustomLoader
import com.ruizdev.pokeapptest.util.enums.DialogAnim
import com.ruizdev.pokeapptest.util.extensions.showBasicDialog
import com.ruizdev.pokeapptest.util.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {

    private lateinit var binding: FragmentDetailsBinding

    private val viewModel: DetailsViewModel by viewModels()

    private val safeArgs: DetailsFragmentArgs by navArgs()

    private lateinit var mAdapter: TypesAdapter

    private val customLoader: CustomLoader by lazy { CustomLoader(requireContext()) }

    override fun FragmentDetailsBinding.initialize() {
        binding = this

        viewModel.getPokemonDetailsLocal(safeArgs.pokemonId)

        initUI()

        readDatabase()

    }

    private fun initUI() {

        setFavoriteIcon(safeArgs.isFavorite)

        binding.imageViewFavorite.setOnClickListener {
            setFavoriteIcon(!safeArgs.isFavorite)
            viewModel.markFavorite(safeArgs.pokemonId.toLong(), !safeArgs.isFavorite)
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) binding.imageViewFavorite.setImageResource(R.drawable.ic_favorite) else binding.imageViewFavorite.setImageResource(
            R.drawable.ic_no_favorite
        )
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            delay(200)
            viewModel.readPokemonDetails.observe(viewLifecycleOwner) { db ->
                if (db == null) {
                    getApiDetailsPokemon()
                } else {
                    setData(db)
                }
            }
        }
    }

    private fun getApiDetailsPokemon() {

        viewModel.getPokemonDetails(safeArgs.pokemonId)
        viewModel.detailPokemonState.observe(viewLifecycleOwner) { response ->

            when (response) {

                is UIState.Success -> {
                    val data = response.data
                    setData(data)
                }

                is UIState.Error -> {
                    showBasicDialog(
                        getString(R.string.error),
                        getString(R.string.no_info_solicitada),
                        getString(
                            R.string.aceptar
                        ),
                        DialogAnim.ERROR
                    ) {
                        findNavController().popBackStack()
                    }
                }

                is UIState.Loading -> {
                    when (response.status) {
                        true -> customLoader.show()
                        false -> customLoader.getDialog().cancel()
                    }
                }

                else -> Unit

            }

        }

    }

    private fun setData(apiResponsePokemonDetail: ApiResponsePokemonDetail?) {

        apiResponsePokemonDetail?.let { detail ->

            with(binding) {

                Glide.with(requireContext())
                    .load(detail.sprites.front_default)
                    .error(R.drawable.ic_image)
                    .placeholder(R.drawable.ic_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)

                tvPokemonName.text = detail.name.replaceFirstChar(Char::titlecase)
                tvInfoWeight.text = getString(
                    R.string.pokemon_peso_kg,
                    calculateMetersAndKilograms(detail.weight.toFloat())
                )
                tvInfoHeight.text = getString(
                    R.string.pokemon_altura_m,
                    calculateMetersAndKilograms(detail.height.toFloat())
                )

            }

            initRecyclerView(detail.types)

        }

    }

    private fun calculateMetersAndKilograms(heightOrWeight: Float): Float {
        return (heightOrWeight / 10)
    }

    private fun initRecyclerView(types: List<Types>) {

        binding.recycleViewPokemonTypes.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            mAdapter = TypesAdapter()
            adapter = mAdapter
        }

        mAdapter.submitList(types)

    }

}