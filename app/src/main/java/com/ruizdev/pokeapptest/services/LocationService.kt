package com.ruizdev.pokeapptest.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    private var notificationManager: NotificationManager? = null

    private var location: Location? = null

    private var sessionId = ""

    override fun onCreate() {
        super.onCreate()

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

        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(notificationChannel)

        }

    }

    @Suppress("MissingPermission")
    fun createLocationRequest() {

        try {
            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest!!, locationCallback!!, null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun removeLocationUpdates() {

        locationCallback?.let { callback ->
            fusedLocationProviderClient?.removeLocationUpdates(callback)
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

}