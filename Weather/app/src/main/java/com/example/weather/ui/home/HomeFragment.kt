package com.example.weather.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.models.Constants.MBAR
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    lateinit var myViewModel: ViewModel
    lateinit var myViewModelFactory: ViewModelFactory
    lateinit var progressDialog: DialogFragment
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

        myViewModelFactory = ViewModelFactory(Reposatory.getInstance(Client.getInstance()))

        myViewModel =
            ViewModelProvider(this.requireActivity(), myViewModelFactory)[HomeViewModel::class.java]
        (myViewModel as HomeViewModel).getWeatherFromApi(22.2, 22.7, "exclude", "appid")
        (myViewModel as HomeViewModel).currentWeather.observe(viewLifecycleOwner) {
            _binding?.country?.text = it.timezone
            _binding?.tempDay?.text = it.current.temp  + com.example.weather.models.Constants.CELSIUS
            val dayhome=getCurrentDay(it.current.dt.toInt())
            _binding?.desc?.text = it.current.weather.get(0).description
            _binding?.day?.text= dayhome
          _binding?.descCard?.text= it.current.temp +com.example.weather.models.Constants.CELSIUS
            binding.descCard2.text =  it.current.humidity.toString() + "%"

            Glide.with(requireActivity()).load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png").into(binding.icon)

            _binding?.descCard3?.text = it.current.wind_speed.toString() + com.example.weather.models.Constants.WINDSPEED

            _binding?.descCard4?.text = it.current.pressure.toString() + MBAR
            var time= formatTime(it.current.sunrise)
            var time2= formatTime(it.current.sunset)
            _binding?.descCard5?.text = time
            _binding?.descCard6?.text = time2
            binding.recDays.apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = DailyAdapter(it.daily)
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

    fun getCurrentDay( dt: Int) : String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar=Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format=SimpleDateFormat("EEEE")
        return format.format(calendar.time)
    }
    fun formatTime(dateObject: Long?): String {
        val date = Date(dateObject!! * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
}

