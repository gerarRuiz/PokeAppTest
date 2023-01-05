package com.ruizdev.pokeapptest.presentation.fragments.maps

import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.FragmentMapsBinding
import com.ruizdev.pokeapptest.presentation.common.BaseFragment

@ExperimentalPagingApi
class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate),
    OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding

    private lateinit var map: GoogleMap

    private val safeArgs: MapsFragmentArgs by navArgs()

    private var latitude = 0.0
    private var longitude = 0.0

    override fun FragmentMapsBinding.initialize() {
        binding = this

        initLocationInfo()
        createFragment()


    }

    private fun initLocationInfo() {
        latitude = safeArgs.latitude.toDouble()
        longitude = safeArgs.longitude.toDouble()
    }

    private fun createFragment() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    private fun createMarker() {
        val coordinates = com.google.android.gms.maps.model.LatLng(latitude, longitude)
        val marker = MarkerOptions().position(coordinates)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18.0f),
            4000,
            null
        )
    }

}