package com.example.weather.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.example.weather.R
import com.example.weather.models.MyResponce
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*

const val PERMISSION_ID = 44

class Gps: AppCompatActivity() {

    lateinit var geocoder: Geocoder
    private lateinit var fusedClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        geocoder = Geocoder(this, Locale.getDefault())
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

    }

    override fun onResume() {
        super.onResume()

    }

    @SuppressLint("MissingPermission")
    private  fun  requestNewLocationDate(){
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        fusedClient.requestLocationUpdates(
            mLocationRequest , mLocationCallback,
            Looper.myLooper()!!
        )
    }


    private val mLocationCallback : LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation : Location = locationResult.lastLocation
            val address = geocoder.getFromLocation(mLastLocation.latitude,mLastLocation.longitude,1)
        }
    }


    private fun checkPermissions():Boolean{
        val result =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(    this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        return result
    }

    private fun isLocationEnabled(): Boolean{val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private  fun  requestPermissions(){
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun  getLastLocation(): Unit{
        if (checkPermissions()){
            if (isLocationEnabled()){
                requestNewLocationDate()
            }else{
                Toast.makeText(this,"turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            requestPermissions()
        }
    }
}
