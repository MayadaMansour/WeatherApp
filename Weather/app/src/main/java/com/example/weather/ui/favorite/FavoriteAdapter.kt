package com.example.weather.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.ItemFavoriteBinding
import com.example.weather.models.MyResponce


class FavoriteAdapter(private var weather: List<MyResponce>, var action: OnClick) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    lateinit var context: Context
    lateinit var binding: ItemFavoriteBinding

    class ViewHolder(var binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemFavoriteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weather.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = weather.get(position)
        holder.binding.deleteIcon.setOnClickListener { action.sendData(currentObj) }
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${currentObj.current.weather.get(0).icon}@2x.png")
            .into(holder.binding.iconFav)
        holder.binding.daesFav.text = currentObj.timezone
        holder.binding.countryFav.text = currentObj.timezone
    }
}