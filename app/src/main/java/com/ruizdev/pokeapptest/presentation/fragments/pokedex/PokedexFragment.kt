package com.ruizdev.pokeapptest.presentation.fragments.pokedex

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.FragmentPokedexBinding
import com.ruizdev.pokeapptest.presentation.adapters.LoaderAdapter
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
            adapter = mAdapter.withLoadStateHeaderAndFooter(
                footer = LoaderAdapter(),
                header = LoaderAdapter()
            )

            handlePokedexResult()
        }
    }

    private fun callData() {
        lifecycleScope.launch {
            viewModel.getAllPokemon.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun handlePokedexResult() {

        lifecycleScope.launch {

            mAdapter.loadStateFlow.collectLatest { state ->

                val error = when {
                    state.refresh is LoadState.Error -> state.refresh as LoadState.Error
                    state.prepend is LoadState.Error -> state.prepend as LoadState.Error
                    state.append is LoadState.Error -> state.append as LoadState.Error
                    else -> null
                }

                when {

                    state.refresh is LoadState.Loading -> {
                        binding.shimmerLayout.root.visibility = View.VISIBLE
                        binding.noDataLayout.root.visibility = View.GONE
                    }

                    error != null -> {
                        binding.shimmerLayout.root.visibility = View.GONE
                        binding.noDataLayout.root.visibility = View.VISIBLE
                        binding.noDataLayout.tvNoData.text =
                            error.error.localizedMessage

                    }

                    mAdapter.itemCount < 1 -> {
                        binding.shimmerLayout.root.visibility = View.GONE
                        binding.noDataLayout.root.visibility = View.VISIBLE
                        binding.noDataLayout.tvNoData.text = getString(R.string.no_info)
                    }

                    else -> {
                        binding.recyclerViewPokedex.visibility = View.VISIBLE
                        binding.noDataLayout.root.visibility = View.GONE
                        binding.shimmerLayout.root.visibility = View.GONE
                    }

                }

            }

        }
    }


}