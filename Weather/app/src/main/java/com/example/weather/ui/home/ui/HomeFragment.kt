package com.example.weather.ui.home.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.LocalActivityResultRegistryOwner.current
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.LocalSource.ConcreteLocalSource
import com.example.weather.data.weather.netwok.ApiState
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.ui.detaile.ui.DaitelsHourlyAdapter
import com.example.weather.ui.detaile.ui.DetailsDailyAdapter
import com.example.weather.ui.main.Constants.MBAR
import com.example.weather.ui.home.view.HomeViewModel
import com.example.weather.ui.home.view.ViewModelFactory
import com.example.weather.ui.main.Constants
import com.example.weather.ui.main.Constants.lang
import com.example.weather.ui.main.Utils
import com.example.weather.ui.main.Utils.convertStringToArabic
import com.example.weather.ui.main.Utils.convertToTime
import com.example.weather.ui.main.Utils.getAddress
import com.example.weather.ui.main.Utils.getCurrentSpeed
import com.example.weather.ui.main.Utils.getCurrentTemperature
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*


private val PERMISSION_ID = 40

@Suppress("UNREACHABLE_CODE")
class HomeFragment : Fragment() {
    var lat: Double = 0.0
    var lon: Double = 0.0
    lateinit var language: String
    lateinit var unites: String
    lateinit var myViewModel: HomeViewModel
    lateinit var myViewModelFactory: ViewModelFactory
    lateinit var geocoder: Geocoder
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences


    val args: HomeFragmentArgs by navArgs()


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!


///////////////////////////////////Location Gps
        geocoder = Geocoder(requireActivity(), Locale.getDefault())
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())


//////////////////////////////////Factory
        myViewModelFactory = ViewModelFactory(
            Reposatory.getInstance(
                Client.getInstance(),
                ConcreteLocalSource(requireContext()),
                PreferenceManager.getDefaultSharedPreferences(requireContext())
            )
        )
        myViewModel =
            ViewModelProvider(this.requireActivity(), myViewModelFactory)[HomeViewModel::class.java]

//////////////////////////////////////Observe Home Data///////////////////////////////////////////////
        lifecycleScope.launch {
            myViewModel.currentWeather.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                    }
                    is ApiState.Fail -> {
                        Toast.makeText(
                            requireContext(),
                            "No Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ApiState.Success -> {
                        _binding?.country?.text = it.data.body()?.timezone
                        _binding?.tempMin?.text =
                            Math.ceil(it.data.body()?.current!!.temp).toInt()
                                .toString() + Constants.CELSIUS
                        val dayhome = getCurrentDay(it.data.body()?.current!!.dt?.toInt()!!)
                        _binding?.desc?.text = it.data.body()?.current!!.weather.get(0).description
                        _binding?.day?.text = dayhome
                        _binding?.descCard?.text =
                            it.data.body()?.current!!.temp.toString() + Constants.CELSIUS
                        binding.descCard2.text = it.data.body()?.current!!.humidity.toString() + "%"
                        Glide.with(requireActivity())
                            .load(
                                "https://openweathermap.org/img/wn/${
                                    it.data.body()?.current!!.weather.get(
                                        0
                                    ).icon
                                }@2x.png"
                            )
                            .into(binding.icon)
                        _binding?.descCard3?.text = ("${
                            it.data.body()?.current!!.wind_speed.toBigDecimal()
                                .setScale(2, RoundingMode.UP)
                        } ${Utils.getCurrentSpeed(requireContext())} ")
                        _binding?.descCard4?.text =
                            it.data.body()?.current!!.pressure.toString() + Constants.MBAR
                        var time =
                            Utils.convertToTime(it.data.body()?.current!!.sunrise!!.toLong(), "en")
                        var time2 =
                            Utils.convertToTime(it.data.body()?.current!!.sunset!!.toLong(), "en")
                        _binding?.descCard5?.text = time
                        _binding?.descCard6?.text = time2


                        //Observe RecyclerView
                        binding.recDays.apply {
                            layoutManager = LinearLayoutManager(context)
                            this.adapter = DetailsDailyAdapter(it.data.body()?.daily!!)
                        }
                        binding.recHours.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            this.adapter = DaitelsHourlyAdapter(it.data.body()?.hourly!!)
                        }


////////////////////Set_Language_Arabic_English////////////////////////////////////////////////////////////////////////////////////////////////////////
                        val lang =
                            sharedPreferences.getString(
                                Constants.lang,
                                Constants.Enum_language.en.toString()
                            )
                        if (lang == Constants.Enum_language.en.toString()) {
                            //en
                            _binding!!.tempMin.text =
                                "${it.data.body()?.current!!.temp} ${
                                    Utils.getCurrentTemperature(
                                        requireContext()
                                    )
                                }"
                            if (it.data.body()!!.daily!![0].temp.min != it.data.body()!!.daily!![0].temp.max)
                                _binding!!.desc.text =
                                    "${it.data.body()!!.daily!![0].temp.min}${
                                        Utils.getCurrentTemperature(
                                            requireContext()
                                        )
                                    }/${
                                        it.data.body()!!.daily!!.get(0).temp.max
                                    }${Utils.getCurrentTemperature(requireContext())}"
                            else
                                _binding!!.desc.text = ""
                            _binding!!.tempMin.text =
                                "${it.data.body()?.current!!.temp}${
                                    Utils.getCurrentTemperature(
                                        requireContext()
                                    )
                                }"
                        } else {
                            //ar
                            _binding!!.tempMin.text =
                                "${Utils.convertStringToArabic(it.data.body()?.current!!.temp.toString())}${
                                    Utils.getCurrentTemperature(requireContext())
                                }"
                            if (it.data.body()!!.daily!![0].temp.min != it.data.body()!!.daily!![0].temp.max)
                                _binding!!.desc.text =
                                    "${Utils.convertStringToArabic(it.data.body()!!.daily!![0].temp.min.toString())}${
                                        Utils.getCurrentTemperature(requireContext())
                                    }/${Utils.convertStringToArabic(it.data.body()!!.daily!!.get(0).temp.max.toString())}${
                                        Utils.getCurrentTemperature(
                                            requireContext()
                                        )
                                    }"
                            else
                                _binding!!.desc.text = ""
                            _binding!!.tempMin.text =
                                "${Utils.convertStringToArabic(it.data.body()?.current!!.temp.toString())}${
                                    Utils.getCurrentTemperature(requireContext())
                                }"
                        }


                        // _binding.container.setBackgroundResource(setBackgroundContainer(it.current.weather[0].icon,requireContext()))


                    }
                }
            }
        }


        return root
    }


//-----------------------------------------------------------------------------------------------------------------------------------------//

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    ///////////////////////////////////////Location Gps Functions/////////////////////////////////////////////////////////
    override fun onResume() {
        super.onResume()
        if (args.map) {
            Log.i("TAG", "onResume: ")
            lat = args.lat.toDouble()
            lon = args.long.toDouble()
            language = "en"
            unites = "standard"
            myViewModel.getWeatherFromApi(
                lat,
                lon,
                "exclude", "3b2709ccc4bdcdaac7f7ea5c91b3da94", language, unites
            )
        } else {
            getLastLocation()
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------//
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            (myViewModel as HomeViewModel).getWeatherFromApi(
                mLastLocation.latitude,
                mLastLocation.longitude,
                "exclude", "3b2709ccc4bdcdaac7f7ea5c91b3da94", language, unites
            )
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE)!! as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )

    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        mFusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                    CancellationTokenSource().token

                override fun isCancellationRequested(): Boolean = false
            }
        ).addOnSuccessListener { location: Location? ->
            if (location == null)
                Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {
                lat = location.latitude
                lon = location.longitude
                myViewModel.getWeatherFromApi(
                    lat,
                    lon,
                    "exclude",
                    "3b2709ccc4bdcdaac7f7ea5c91b3da94",
                    "en",
                    "standard"
                )

            }
        }
    }

    fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(context, "turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)

            }
        } else {
            requestPermission()
        }
    }


    //Methods Data
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDay(dt: Int): String {
        val date = Date(dt * 1000L)
        val sdf = SimpleDateFormat("d")
        sdf.timeZone = TimeZone.getDefault()
        val formatedData = sdf.format(date)
        val intDay = formatedData.toInt()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, intDay)
        val format = SimpleDateFormat("EEEE")
        return format.format(calendar.time)
    }


    //----------------------------------------------------------------------------------------------------------------------------------//
    //OnlineChick
    private fun isOnline(ctx: Context): Boolean {
        val cm =
            ctx.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null
    }

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

}

