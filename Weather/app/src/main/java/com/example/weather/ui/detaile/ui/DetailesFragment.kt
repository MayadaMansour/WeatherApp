package com.example.weather.ui.detaile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.LocalSource.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentDetailesBinding
import com.example.weather.ui.main.Constants
import com.example.weather.ui.detaile.view.DetailesViewModel
import com.example.weather.ui.detaile.view.DetailesViewModelFactory
import com.example.weather.ui.main.Utils
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*


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
        myViewModel.currentWeather.observe(viewLifecycleOwner) {
            _binding?.country?.text = it.timezone
            _binding?.tempDay?.text =
                Math.ceil(it.current.temp).toInt().toString() + Constants.CELSIUS
            val dayhome = getCurrentDay(it.current.dt.toInt())
            _binding?.desc?.text = it.current.weather.get(0).description
            _binding?.day?.text = dayhome
            _binding?.descCard?.text =
                it.current.temp.toString() + Constants.CELSIUS
            binding.descCard2.text = it.current.humidity.toString() + "%"
            Glide.with(requireActivity())
                .load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png")
                .into(binding.icon)
            _binding?.descCard3?.text = ("${
                it.current.wind_speed.toBigDecimal().setScale(2, RoundingMode.UP)
            } ${Utils.getCurrentSpeed(requireContext())} ")
            _binding?.descCard4?.text = it.current.pressure.toString() + Constants.MBAR
            var time = Utils.convertToTime(it.current.sunrise!!.toLong(), "en")
            var time2 = Utils.convertToTime(it.current.sunset!!.toLong(), "en")
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


////////////////////Set_Language_Arabic_English////////////////////////////////////////////////////////////////////////////////////////////////////////
            val lang =
                sharedPreferences.getString(Constants.lang, Constants.Enum_language.en.toString())
            if (lang == Constants.Enum_language.en.toString()) {
                //en
                _binding!!.tempDay.text =
                    "${it.current.temp} ${Utils.getCurrentTemperature(requireContext())}"
                if (it.daily!![0].temp.min != it.daily[0].temp.max)
                    _binding!!.desc.text =
                        "${it.daily[0].temp.min}${Utils.getCurrentTemperature(requireContext())}/${
                            it.daily.get(0).temp.max
                        }${Utils.getCurrentTemperature(requireContext())}"
                else
                    _binding!!.desc.text = ""
                _binding!!.tempDay.text =
                    "${it.current.temp}${Utils.getCurrentTemperature(requireContext())}"
            } else {
                //ar
                _binding!!.tempDay.text =
                    "${Utils.convertStringToArabic(it.current.temp.toString())}${
                        Utils.getCurrentTemperature(requireContext())
                    }"
                if (it.daily!![0].temp.min != it.daily[0].temp.max)
                    _binding!!.desc.text =
                        "${Utils.convertStringToArabic(it.daily[0].temp.min.toString())}${
                            Utils.getCurrentTemperature(requireContext())
                        }/${Utils.convertStringToArabic(it.daily.get(0).temp.max.toString())}${
                            Utils.getCurrentTemperature(
                                requireContext()
                            )
                        }"
                else
                    _binding!!.desc.text = ""
                _binding!!.tempDay.text =
                    "${Utils.convertStringToArabic(it.current.temp.toString())}${
                        Utils.getCurrentTemperature(requireContext())
                    }"
            }


            // _binding.container.setBackgroundResource(setBackgroundContainer(it.current.weather[0].icon,requireContext()))


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




