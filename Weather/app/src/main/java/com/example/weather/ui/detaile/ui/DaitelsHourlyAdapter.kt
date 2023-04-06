package com.example.weather.ui.detaile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.ItemHoursBinding
import com.example.weather.models.CurrentWeather
import com.example.weather.ui.main.Constants
import com.example.weather.ui.main.Utils
import java.util.*



class DaitelsHourlyAdapter(var hours: List<CurrentWeather>) :
    RecyclerView.Adapter<DaitelsHourlyAdapter.ViewHolder>() {
    lateinit var current: Context
    lateinit var binding: ItemHoursBinding

    class ViewHolder(var binding: ItemHoursBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        current = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemHoursBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hours.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = hours.get(position)
        /*   var timeHour = getCurrentTime(currentObj.dt.toInt())
          Glide.with(_current)
              .load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png")
              .into(holder.binding.iconHour)
          holder.binding.tempHours.text =
              currentObj.temp.toString() + Constants.CELSIUS*/


        val sharedPreferences = holder.binding.tempHours.context.getSharedPreferences(
            "My Shared",
            Context.MODE_PRIVATE
        )!!
        val language = sharedPreferences.getString(Constants.lang, "en")!!
        holder.binding.tempHours.text = currentObj.temp.toString() + Constants.CELSIUS
        if (language.equals("en")) {
            holder.binding.tempHours.text =
                "${currentObj.temp}${Utils.getCurrentTemperature(current)}"
        } else {
            holder.binding.tempHours.text =
                "${Utils.convertStringToArabic(currentObj.temp.toString())}${
                    Utils.getCurrentTemperature(
                        current
                    )
                }"
        }

        holder.binding.timeHour.text = Utils.convertToTime(currentObj.dt, language)

        Glide.with(current)
            .load("https://openweathermap.org/img/wn/${currentObj.weather.get(0).icon}@2x.png")
            .into(holder.binding.iconHour)

    }

    /*  @SuppressLint("SimpleDateFormat")
      fun getCurrentTime(dt: Int): String {
          var date = Date(dt * 1000L)
          var sdf = SimpleDateFormat("hh:mm a")
          sdf.timeZone = TimeZone.getDefault()
          return sdf.format(date)
      } */
}