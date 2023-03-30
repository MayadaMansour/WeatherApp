package com.example.weather.ui.home.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.ItemDaysBinding
import com.example.weather.models.Daily

import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter( var current: List<Daily>) : RecyclerView.Adapter<DailyAdapter.ViewHolder>(){
    lateinit var context: Context
    lateinit var binding: ItemDaysBinding

    class ViewHolder (var binding :ItemDaysBinding): RecyclerView.ViewHolder(binding.root) {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= ItemDaysBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return  current.size
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = current.get(position)
        Glide.with(context).load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png").into(holder.binding.iconDay)
        val max = Math.ceil(currentObj.temp.max).toInt()
        val min = Math.ceil(currentObj.temp.min).toInt()
        binding.tempDay.text="$max/$min°C"
        val date= Date(currentObj.dt*1000L)
        val sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        val formatedData=sdf.format(date)
        val calendar=Calendar.getInstance()
        val intDay=formatedData.toInt()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        val format=SimpleDateFormat(/* pattern = */ "EEEE")
        val day=format.format(calendar.time)
        binding.countryDay.text=day
        holder.binding.daesDay.text= currentObj.weather.get(0).description
    }
}

