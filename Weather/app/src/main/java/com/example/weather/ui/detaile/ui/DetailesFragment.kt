package com.example.weather.ui.detaile.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mvvm.Model.Reposatory
import com.example.weather.R
import com.example.weather.data.weather.local.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentDetailesBinding
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.models.Constants
import com.example.weather.ui.detaile.view.DetailesViewModel
import com.example.weather.ui.detaile.view.DetailesViewModelFactory
import com.example.weather.ui.home.ui.DailyAdapter
import com.example.weather.ui.home.ui.HoursAdapter
import com.example.weather.ui.home.view.HomeViewModel
import com.example.weather.ui.home.view.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*


class DetailesFragment : Fragment() {

    lateinit var myViewModel: DetailesViewModel
    lateinit var myViewModelFactory: DetailesViewModelFactory
    var latitude = 0.0
    var longitude = 0.0
    val args: DetailesFragmentArgs by navArgs()
    lateinit var set: SharedPreferences
    private var _binding: FragmentDetailesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        latitude=args.lat.toDouble()
        longitude=args.lat.toDouble()

    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //Factory
        myViewModelFactory = DetailesViewModelFactory(
            Reposatory.getInstance(
                Client.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )

        myViewModel =
            ViewModelProvider(
                this.requireActivity(),
                myViewModelFactory
            )[DetailesViewModel::class.java]


        myViewModel.getWeatherFromApi(latitude, longitude, "exclude", "3b2709ccc4bdcdaac7f7ea5c91b3da94")


        //Observe Home Data
        myViewModel.currentWeather.observe(viewLifecycleOwner) {
            _binding?.country?.text = it.timezone
            _binding?.tempDay?.text =
                it.current.temp.toString() + Constants.CELSIUS
            val dayhome = getCurrentDay(it.current.dt.toInt())
            _binding?.desc?.text = it.current.weather.get(0).description
            _binding?.day?.text = dayhome
            _binding?.descCard?.text =
                it.current.temp.toString() + Constants.CELSIUS
            binding.descCard2.text = it.current.humidity.toString() + "%"
            Glide.with(requireActivity())
                .load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png")
                .into(binding.icon)
            _binding?.descCard3?.text =
                it.current.wind_speed.toString() + Constants.WINDSPEED
            _binding?.descCard4?.text = it.current.pressure.toString() + Constants.MBAR
            var time = formatTime(it.current.sunrise)
            var time2 = formatTime(it.current.sunset)
            _binding?.descCard5?.text = time
            _binding?.descCard6?.text = time2


            //Observe RecyclerView
            binding.recDays.apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = DetailsDailyAdapter(it.daily!!)
            }
            binding.recHours.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = DaitelsHourlyAdapter(it.hourly)
            }
        }

        return root
    }


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

    @SuppressLint("SimpleDateFormat")
    fun formatTime(dateObject: Long?): String {
        val date = Date(dateObject!! * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
}




