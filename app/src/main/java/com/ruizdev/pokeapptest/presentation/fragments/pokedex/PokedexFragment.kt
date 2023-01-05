package com.ruizdev.pokeapptest.presentation.fragments.pokedex

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruizdev.pokeapptest.databinding.FragmentPokedexBinding
import com.ruizdev.pokeapptest.presentation.adapters.PokemonAdapter
import com.ruizdev.pokeapptest.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokedexFragment : BaseFragment<FragmentPokedexBinding>(FragmentPokedexBinding::inflate) {

    private lateinit var binding: FragmentPokedexBinding

    private val viewModel: PokedexViewModel by viewModels()

    private lateinit var mAdapter: PokemonAdapter

    private var textColor = -1
    private var backgroundColor = -1

    override fun FragmentPokedexBinding.initialize() {
        binding = this

        readDataStoreData()

    }

    private fun readDataStoreData() {

        lifecycleScope.launch {
            viewModel.textColor.collectLatest { color ->
                delay(150)
                textColor = color
            }
        }

        lifecycleScope.launch {
            viewModel.backgroundColor.collectLatest { color ->
                delay(150)
                backgroundColor = color
                initRecyclerViewPokedex()
                callData()
            }
        }

    }

    private fun initRecyclerViewPokedex() {
        binding.recyclerViewPokedex.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            mAdapter = PokemonAdapter(
                textColor = textColor,
                backgroundColor = backgroundColor,
                { pokemon ->

                    val action = PokedexFragmentDirections.actionPokedexFragment2ToDetailsFragment(pokemon?.id ?: 1, pokemon?.favorite ?: false)
                    findNavController().navigate(action)

                }, { pokemon ->

                    pokemon?.let { value ->

                        viewModel.markFavorite(
                            pokemonId = value.id.toLong(),
                            favorite = !value.favorite
                        )

                    }

                }, { idPokemon ->

                    idPokemon?.let { id ->

                        val action = PokedexFragmentDirections.actionPokedexFragment2ToImageFragment(id)
                        findNavController().navigate(action)

                    }

                }
            )
            adapter = mAdapter
        }
    }

    private fun callData() {
        lifecycleScope.launch {
            viewModel.getAllPokemon.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}