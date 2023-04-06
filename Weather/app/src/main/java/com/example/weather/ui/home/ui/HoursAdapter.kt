package com.example.weather.ui.home.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.ItemHoursBinding
import com.example.weather.models.CurrentWeather
import com.example.weather.ui.main.Constants
import com.example.weather.ui.main.Utils.convertStringToArabic
import com.example.weather.ui.main.Utils.convertToTime
import com.example.weather.ui.main.Utils.getCurrentTemperature
import java.text.SimpleDateFormat
import java.util.*


class HoursAdapter(var hour: List<CurrentWeather>) :
    RecyclerView.Adapter<HoursAdapter.ViewHolder>() {
    lateinit var context: Context
    lateinit var binding: ItemHoursBinding


    class ViewHolder(var binding: ItemHoursBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemHoursBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hour.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = hour.get(position)

        val sharedPreferences = holder.binding.tempHours.context.getSharedPreferences(
            "My Shared",
            Context.MODE_PRIVATE
        )!!
        val language = sharedPreferences.getString(Constants.lang, "en")!!
        holder.binding.tempHours.text = currentObj.temp.toString() + Constants.CELSIUS
        if (language.equals("en")) {
            holder.binding.tempHours.text = "${currentObj.temp}${getCurrentTemperature(context)}"
        } else {
            holder.binding.tempHours.text =
                "${convertStringToArabic(currentObj.temp.toString())}${getCurrentTemperature(context)}"
        }


        holder.binding.timeHour.text = convertToTime(currentObj.dt, language)

        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png")
            .into(holder.binding.iconHour)


    }

}