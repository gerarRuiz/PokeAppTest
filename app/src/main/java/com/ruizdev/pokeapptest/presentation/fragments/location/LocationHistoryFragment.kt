package com.ruizdev.pokeapptest.presentation.fragments.location

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ruiz.emovie.presentation.fragments.location.LocationHistoryViewModel
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.FragmentLocationHistoryBinding
import com.ruizdev.pokeapptest.domain.model.LocationEvent
import com.ruizdev.pokeapptest.presentation.adapters.LocationAdapter
import com.ruizdev.pokeapptest.presentation.common.BaseFragment
import com.ruizdev.pokeapptest.util.Constants.COLL_LOCATION
import com.ruizdev.pokeapptest.util.Constants.COLL_LOCATIONS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class LocationHistoryFragment :
    BaseFragment<FragmentLocationHistoryBinding>(FragmentLocationHistoryBinding::inflate) {

    private lateinit var binding: FragmentLocationHistoryBinding

    private lateinit var fireStoreListener: ListenerRegistration

    private lateinit var adapterLocation: LocationAdapter

    private val viewModel: LocationHistoryViewModel by viewModels()

    private var sessionId = ""

    override fun FragmentLocationHistoryBinding.initialize() {
        binding = this
        configRecyclerView()
        initSessionId()

    }

    override fun onResume() {
        super.onResume()
        if (sessionId.isNotEmpty()) configFireStoreRealtime()
    }

    private fun initSessionId() {
        lifecycleScope.launch {
            viewModel.sessionId.collectLatest {
                sessionId = it
                delay(500)
            }
        }
    }

    private fun configRecyclerView() {

        adapterLocation = LocationAdapter(mutableListOf()) { location ->
            val action =
                LocationHistoryFragmentDirections.actionLocationHistoryFragmentToMapsFragment(
                    latitude = location?.latitude ?: "0.0",
                    longitude = location?.longitude ?: "0.0"
                )
            findNavController().navigate(action)
        }

        binding.recyclerViewLocations.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterLocation
        }

    }

    private fun configFireStoreRealtime() {

        val db = Firebase.firestore
        val locationRef =
            db.collection(COLL_LOCATIONS).document(sessionId)
                .collection(COLL_LOCATION)

        fireStoreListener = locationRef.addSnapshotListener { value, error ->

            if (error != null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.text_error_consultar_datos),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@addSnapshotListener
            }

            for (snapshot in value!!.documentChanges) {

                val location = snapshot.document.toObject(LocationEvent::class.java)
                location.id = snapshot.document.id

                when (snapshot.type) {

                    DocumentChange.Type.ADDED -> adapterLocation.add(location)
                    DocumentChange.Type.MODIFIED -> adapterLocation.update(location)
                    DocumentChange.Type.REMOVED -> adapterLocation.delete(location)

                }

            }

        }


    }

}