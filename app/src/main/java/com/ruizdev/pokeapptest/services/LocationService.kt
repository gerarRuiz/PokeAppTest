package com.ruizdev.pokeapptest.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.SettingsClient
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.domain.model.LocationEvent
import com.ruizdev.pokeapptest.util.Constants.ACTION_START_LOCATION_SERVICE
import com.ruizdev.pokeapptest.util.Constants.ACTION_STOP_LOCATION_SERVICE
import com.ruizdev.pokeapptest.util.Constants.COLL_LOCATION
import com.ruizdev.pokeapptest.util.Constants.COLL_LOCATIONS
import com.ruizdev.pokeapptest.util.Constants.LOCATION_INTERVAL
import com.ruizdev.pokeapptest.util.Constants.LOCATION_INTERVAL_FASTEST
import com.ruizdev.pokeapptest.util.Constants.NOTIFICATION_CHANNEL_ID
import com.ruizdev.pokeapptest.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.ruizdev.pokeapptest.util.Constants.NOTIFICATION_ID
import com.ruizdev.pokeapptest.util.ConstantsDialogs
import java.util.*

class LocationService : Service() {

    private val TAG = "LocationService"
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null

    private var huaweiFusedLocationProviderClient: com.huawei.hms.location.FusedLocationProviderClient? =
        null
    private var huaweiSettingClient: SettingsClient? = null
    private var huaweiLocationCallback: com.huawei.hms.location.LocationCallback? = null
    private var huaweiLocationRequest: com.huawei.hms.location.LocationRequest? = null

    private var notificationManager: NotificationManager? = null

    private var location: Location? = null

    private var sessionId = ""

    override fun onCreate() {
        super.onCreate()

        if (!isHmsAvailable(this)) {

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            locationRequest =
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_INTERVAL_FASTEST)
                    .setIntervalMillis(LOCATION_INTERVAL)
                    .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    onNewLocation(locationResult)
                }
            }

        } else {

            huaweiFusedLocationProviderClient =
                com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(this)
            huaweiSettingClient = com.huawei.hms.location.LocationServices.getSettingsClient(this)
            huaweiLocationRequest = com.huawei.hms.location.LocationRequest().apply {
                interval = LOCATION_INTERVAL
                needAddress = true
                priority = com.huawei.hms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            huaweiLocationCallback = object : com.huawei.hms.location.LocationCallback() {
                override fun onLocationResult(locationResult: com.huawei.hms.location.LocationResult?) {
                    super.onLocationResult(locationResult)
                    huaweiOnNewLocation(locationResult)
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    super.onLocationAvailability(p0)
                    Log.i(TAG, "onLocationAvailability: ${p0?.isLocationAvailable}")
                }
            }

        }

        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(notificationChannel)

        }

        startForeground(NOTIFICATION_ID, getNotification())

    }

    @Suppress("MissingPermission")
    fun createLocationRequest() {

        if (!isHmsAvailable(this)) {
            try {
                fusedLocationProviderClient?.requestLocationUpdates(
                    locationRequest!!, locationCallback!!, null
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                huaweiFusedLocationProviderClient?.requestLocationUpdates(
                    huaweiLocationRequest!!, huaweiLocationCallback!!, Looper.getMainLooper()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    private fun removeLocationUpdates() {

        if (!isHmsAvailable(this)) {
            locationCallback?.let { callback ->
                fusedLocationProviderClient?.removeLocationUpdates(callback)
            }
        } else {
            huaweiLocationCallback?.let { callback ->
                huaweiFusedLocationProviderClient?.removeLocationUpdates(callback)
            }
        }
        stopForeground(true)
        stopSelf()

    }

    private fun onNewLocation(locationResult: LocationResult) {
        location = locationResult.lastLocation
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address =
            geoCoder.getFromLocation(location?.latitude ?: 0.0, location?.longitude ?: 0.0, 1)

        val longitud = address?.get(0)?.longitude
        val latitud = address?.get(0)?.latitude
        val adddres = address?.get(0)?.getAddressLine(0)
        val city = address?.get(0)?.locality
        val country = address?.get(0)?.countryName

        val location = LocationEvent(
            longitude = longitud.toString(),
            latitude = latitud.toString(),
            address = adddres ?: "",
            city = city ?: "",
            country = country ?: ""
        )

        val time = Date().time.toString()

        val db = Firebase.firestore
        db.collection(COLL_LOCATIONS).document(sessionId)
            .collection(COLL_LOCATION)
            .document(time)
            .set(location)
            .addOnSuccessListener {
                Log.i(TAG, "onNewLocation: Location Added")
            }
            .addOnFailureListener {
                Log.i(TAG, "onNewLocation: Location Failed to add")
            }

        startForeground(NOTIFICATION_ID, getNotification())
    }

    private fun huaweiOnNewLocation(locationResult: com.huawei.hms.location.LocationResult?) {

        locationResult?.let { result ->

            val locations = result.locations
            if (locations.isNotEmpty()) {
                val huaweiLocation = locations[0]
                val geoCoder = Geocoder(this, Locale.getDefault())
                val address =
                    geoCoder.getFromLocation(huaweiLocation.latitude, huaweiLocation.longitude, 1)

                val longitud = address?.get(0)?.longitude
                val latitud = address?.get(0)?.latitude
                val adddres = address?.get(0)?.getAddressLine(0)
                val city = address?.get(0)?.locality
                val country = address?.get(0)?.countryName

                val location = LocationEvent(
                    longitude = longitud.toString(),
                    latitude = latitud.toString(),
                    address = adddres ?: "",
                    city = city ?: "",
                    country = country ?: ""
                )

                val time = Date().time.toString()

                Log.i(TAG, "huaweiOnNewLocation: $location")

                startForeground(NOTIFICATION_ID, getNotification())

            }
        }

    }

    private fun getNotification(): Notification {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.location_updated))
            .setContentText(
                "Latitud -> ${location?.latitude}\nLongitud -> ${location?.longitude}"
            )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL_ID)
        }

        return notification.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            val action = intent.action
            sessionId = intent.getStringExtra(ConstantsDialogs.DIALOG_SESSION_ID) ?: ""
            Log.i(TAG, "onStartCommand: $sessionId")
            if (action != null) {
                if (action == ACTION_START_LOCATION_SERVICE) {
                    createLocationRequest()
                } else if (action == ACTION_STOP_LOCATION_SERVICE) {
                    removeLocationUpdates()
                }
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    private fun isHmsAvailable(context: Context?): Boolean {

        var isAvailable = false

        context?.let { mContext ->
            val result =
                HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(mContext)
            isAvailable = (com.huawei.hms.api.ConnectionResult.SUCCESS == result)
        }

        Log.i(TAG, "isHmsAvailable: $isAvailable")
        return isAvailable
    }

}