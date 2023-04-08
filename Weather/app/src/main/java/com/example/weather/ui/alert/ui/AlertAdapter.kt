package com.example.weather.ui.alert.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemAlertBinding
import com.example.weather.models.Alert
import com.example.weather.ui.main.Utils


class AlertAdapter(
    var list: List<Alert>?,
    var context: Context,
    val onClick: (Alert) -> Unit
) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {

    lateinit var binding: ItemAlertBinding

    class ViewHolder(var binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemAlertBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = list!!.get(position)
        holder.binding.countryAlert.text = currentObj.AlertCityName
        holder.binding.startTime.text = Utils.formatTimeAlert(currentObj.startDay)
        if (Utils.isDaily(currentObj.startDay, currentObj.endDay)) {
            holder.binding.endTime.text = Utils.formatTimeAlert(currentObj.endDay)
        } else {
            holder.binding.endTime.text = Utils.formatTimeAlert(currentObj.endDay)

        }
        if (Utils.isOnline(context)) {
            holder.binding.deleteAlert.isEnabled = true
            holder.binding.deleteAlert.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Delete Alarm")
                    .setMessage("Do you really want to delete alarm ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, whichButton ->
                            onClick(currentObj!!)

                        })
                    .setNegativeButton(android.R.string.no, null).show()
            }
        } else {
            holder.binding.deleteAlert.isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

}