package com.ruizdev.pokeapptest.presentation

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.ActivityMainBinding
import com.ruizdev.pokeapptest.services.LocationService
import com.ruizdev.pokeapptest.util.Constants.ACTION_START_LOCATION_SERVICE
import com.ruizdev.pokeapptest.util.Constants.NOTIFICATION_ID
import com.ruizdev.pokeapptest.util.ConstantsDialogs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private var locationService: Intent? = null

    private var sessionId = ""

    private var isCoarseLocationPermissionGranted = false
    private var isFineLocationPermissionGranted = false
    private var isNotificationPermissionGranted = false
    private var isBackgroundPermissionGranted = false

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            when {

                it[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {

                    isCoarseLocationPermissionGranted = true

                }

                it[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    isFineLocationPermissionGranted = true
                }

                it[Manifest.permission.POST_NOTIFICATIONS] == true -> {
                    isNotificationPermissionGranted = true
                }

                it[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true -> {
                    isBackgroundPermissionGranted = true
                }

            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSessionUUID()

        setupBottomNav()

        checkLocationPermissions()

    }

    private fun initSessionUUID() {
        lifecycleScope.launch {
            viewModel.sessionUUID.collectLatest {
                sessionId = it
                delay(500)
                if (sessionId.isEmpty()){
                    val uuid = abs((0..999999999999).random())
                    sessionId = uuid.toString()
                    viewModel.saveSessionUUID(uuid.toString())
                }
                locationService = Intent(this@MainActivity, LocationService::class.java).apply {
                    action = ACTION_START_LOCATION_SERVICE
                    putExtra(ConstantsDialogs.DIALOG_SESSION_ID, it)
                }
                checkLocationPermissions()
            }
        }
    }

    private fun setupBottomNav(){

        val bottomNavigationBar = binding.bottomNavigationBar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, destination, _ ->

            when(destination.id){
                R.id.pokedexFragment2 -> showBottomNav()
                R.id.avatarFragment -> showBottomNav()
                R.id.locationHistoryFragment -> showBottomNav()
                else -> hideBottomNav()
            }

        }

    }

    private fun showBottomNav() {
        binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationBar.visibility = View.GONE
    }

    private fun checkLocationPermissions() {

        isCoarseLocationPermissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        isFineLocationPermissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            isNotificationPermissionGranted = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        val permissionRequest = arrayListOf<String>()

        if (!isCoarseLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (!isFineLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (!isNotificationPermissionGranted){
                permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (!isBackgroundPermissionGranted){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }

        if (permissionRequest.isNotEmpty()){
            permission.launch(permissionRequest.toTypedArray())
        }else{
            if (!foregroundServiceRunning()) {
                if (sessionId.isNotEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(locationService)
                    }
                }
            }
        }

    }

    private fun foregroundServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (item in activityManager.getRunningServices(NOTIFICATION_ID)) {
            if (LocationService::class.simpleName == item.javaClass.simpleName) {
                if (item.foreground) {
                    return true
                }
            }
        }
        return false
    }

}