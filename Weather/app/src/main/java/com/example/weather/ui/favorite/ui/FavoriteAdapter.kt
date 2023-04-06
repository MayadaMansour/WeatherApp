package com.example.weather.ui.favorite.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.ItemFavoriteBinding
import com.example.weather.models.City
import com.example.weather.ui.favorite.OnClick
import com.example.weather.ui.main.Constants
import com.example.weather.ui.main.Utils
import com.example.weather.ui.main.Utils.convertToDate


class FavoriteAdapter(
    private var list: List<City>,
    val onClick: OnClick,

    ) :
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
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = list.get(position)

        holder.binding.deleteIcon.setOnClickListener {
            onClick.deleteWeathers(currentObj)
            notifyDataSetChanged()
        }
        var lat =currentObj.citylat
        var lon =currentObj.citylon
        holder.binding.cardFv.setOnClickListener {
            onClick.sendData(lat,lon)
        }
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${currentObj.city.get(0)}@2x.png")
            .into(holder.binding.iconFav)
        val sharedPreference =  context.getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        val language =  sharedPreference.getString(Constants.lang,"en") !!

      //  holder.binding.daesFav.text = currentObj.city
       // holder. binding.countryFav.text = Utils.convertToDay(currentObj.city, language)

        val date = currentObj.city.let  {
            convertToDate(it.get(0).toLong(),language) }
        if(language=="en"){
            holder.binding.countryFav.text ="${currentObj.city}"
            holder.binding.daesFav.text =" ${date}"
        }else{
            holder.binding.countryFav.text ="${currentObj.city} "
            holder.binding.daesFav.text =" ${date}"
        }

    }

    fun setList(favorite:List<City>){
        list=favorite
        notifyDataSetChanged()
    }


}