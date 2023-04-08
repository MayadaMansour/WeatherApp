package com.example.weather.ui.detaile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.weather.data.weather.netwok.LocalDataStateAlerts
import com.example.weather.databinding.FragmentDetailesBinding
import com.example.weather.ui.main.Constants
import com.example.weather.ui.detaile.view.DetailesViewModel
import com.example.weather.ui.detaile.view.DetailesViewModelFactory
import com.example.weather.ui.main.Utils
import kotlinx.coroutines.flow.collectLatest
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DetailesFragment : Fragment() {

    lateinit var myViewModel: DetailesViewModel
    lateinit var myViewModelFactory: DetailesViewModelFactory
    var latitude = 0.0
    var longitude = 0.0
    var language: String = ""
    var unites: String = ""
    val args: DetailesFragmentArgs by navArgs()
    lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentDetailesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        latitude = args.lat.toDouble()
        longitude = args.lat.toDouble()

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!


        //Factory
        myViewModelFactory = DetailesViewModelFactory(
            Reposatory.getInstance(
                Client.getInstance(),
                ConcreteLocalSource(requireContext()),
                PreferenceManager.getDefaultSharedPreferences(requireContext())
            )
        )

        myViewModel =
            ViewModelProvider(
                this.requireActivity(),
                myViewModelFactory
            )[DetailesViewModel::class.java]


        myViewModel.getWeatherFromApi(
            latitude,
            longitude,
            "exclude",
            "3b2709ccc4bdcdaac7f7ea5c91b3da94",
            language,
            unites
        )


        //Observe Home Data
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
                            .load("https://openweathermap.org/img/wn/${it.data.body()?.current!!.weather.get(0).icon}@2x.png")
                            .into(binding.icon)
                        _binding?.descCard3?.text = ("${
                            it.data.body()?.current!!.wind_speed.toBigDecimal().setScale(2, RoundingMode.UP)
                        } ${Utils.getCurrentSpeed(requireContext())} ")
                        _binding?.descCard4?.text = it.data.body()?.current!!.pressure.toString() + Constants.MBAR
                        var time = Utils.convertToTime(it.data.body()?.current!!.sunrise!!.toLong(), "en")
                        var time2 = Utils.convertToTime(it.data.body()?.current!!.sunset!!.toLong(), "en")
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
                                "${it.data.body()?.current!!.temp} ${Utils.getCurrentTemperature(requireContext())}"
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
                                "${it.data.body()?.current!!.temp}${Utils.getCurrentTemperature(requireContext())}"
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


        }




