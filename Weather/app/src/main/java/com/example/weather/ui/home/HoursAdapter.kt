package com.example.weather.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.ItemHoursBinding
import com.example.weather.models.CurrentWeather
import java.text.SimpleDateFormat
import java.util.*


class HoursAdapter ( var current: List<CurrentWeather>) : RecyclerView.Adapter<HoursAdapter .ViewHolder>(){
    lateinit var _current: Context
    lateinit var binding: ItemHoursBinding

    class ViewHolder (var binding : ItemHoursBinding): RecyclerView.ViewHolder(binding.root) {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _current=parent.context
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= ItemHoursBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  current.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = current.get(position)
        var timeHour = getCurrentTime(currentObj.dt.toInt())
        Glide.with(_current).load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png").into(holder.binding.iconHour)
        holder.binding.tempHours.text = currentObj.temp.toString() + com.example.weather.models.Constants.CELSIUS
        holder.binding.timeHour.text= timeHour
    }
    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(dt: Int) : String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone=TimeZone.getDefault()
        return sdf.format(date)
    }
}