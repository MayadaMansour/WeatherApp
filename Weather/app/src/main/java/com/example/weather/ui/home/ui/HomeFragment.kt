package com.example.weather.ui.home.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.day1.Room.DAO
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.local.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentHomeBinding

import com.example.weather.models.Constants.MBAR
import com.example.weather.ui.home.view.HomeViewModel
import com.example.weather.ui.home.view.ViewModelFactory
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*

private val PERMISSION_ID = 40

@Suppress("UNREACHABLE_CODE")
class HomeFragment : Fragment() {
    lateinit var myViewModel: ViewModel
    lateinit var myViewModelFactory: ViewModelFactory
    lateinit var geocoder: Geocoder
    private lateinit var fusedClient: FusedLocationProviderClient
    lateinit var dao: DAO
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //Location Gps
        geocoder = Geocoder(requireActivity(), Locale.getDefault())
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()


        //Factory
        myViewModelFactory = ViewModelFactory(Reposatory.getInstance(Client.getInstance(), ConcreteLocalSource(requireContext())))
        myViewModel =
            ViewModelProvider(this.requireActivity(), myViewModelFactory)[HomeViewModel::class.java]



        //Observe Home Data
        (myViewModel as HomeViewModel).currentWeather.observe(viewLifecycleOwner) {
            _binding?.country?.text = it.timezone
            _binding?.tempDay?.text = it.current.temp.toString() + com.example.weather.models.Constants.CELSIUS
            val dayhome = getCurrentDay(it.current.dt.toInt())
            _binding?.desc?.text = it.current.weather.get(0).description
            _binding?.day?.text = dayhome
            _binding?.descCard?.text =
                it.current.temp .toString()+ com.example.weather.models.Constants.CELSIUS
            binding.descCard2.text = it.current.humidity.toString() + "%"
            Glide.with(requireActivity())
                .load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png")
                .into(binding.icon)
            _binding?.descCard3?.text =
                it.current.wind_speed.toString() + com.example.weather.models.Constants.WINDSPEED
            _binding?.descCard4?.text = it.current.pressure.toString() + MBAR
            var time = formatTime(it.current.sunrise)
            var time2 = formatTime(it.current.sunset)
            _binding?.descCard5?.text = time
            _binding?.descCard6?.text = time2


            //Observe RecyclerView
            binding.recDays.apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = DailyAdapter(it.daily!!)
            }
            binding.recHours.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = HoursAdapter(it.hourly)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    //Location Gps Functions
    override fun onResume() {
        super.onResume()
        getLastLocation()
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            (myViewModel as HomeViewModel).getWeatherFromApi(
                mLastLocation.latitude,
                mLastLocation.altitude,
                "exclude", "3b2709ccc4bdcdaac7f7ea5c91b3da94"
            )
        }
    }

    private fun checkPermissions(): Boolean {
        val result =
            ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        return result
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        fusedClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }


    //Methods Data
    fun getCurrentDay(dt: Int): String {
        var date = Date(dt * 1000L)
        var sdf = SimpleDateFormat("d")
        sdf.timeZone = TimeZone.getDefault()
        var formatedData = sdf.format(date)
        var intDay = formatedData.toInt()
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, intDay)
        var format = SimpleDateFormat("EEEE")
        return format.format(calendar.time)
    }

    fun formatTime(dateObject: Long?): String {
        val date = Date(dateObject!! * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }

    //OnlineChick
    private fun isOnline(ctx: Context): Boolean {
        val cm =
            ctx.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null
    }


}

