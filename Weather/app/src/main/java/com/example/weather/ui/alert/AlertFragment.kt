package com.example.weather.ui.alert

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.ALARM_SERVICE
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.example.weather.databinding.FragmentAlertBinding
import com.google.android.material.timepicker.MaterialTimePicker


private val CHANNEL_ID:String="CHANNEL_ID"
private val TRANSACTION:String="TRANSACTION_DONE"
class AlertFragment : Fragment() {

    private lateinit var viewModel: AlertViewModel
    private lateinit var picker:MaterialTimePicker
    lateinit var alarmManager: AlarmManager


    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!



    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root
    //    val alarmManager: AlarmManager = (AlarmManager) getSystemService (ALARM_SERVICE)

        createNotification()
        binding.alertSet.setOnClickListener {

            


        }

        return root

    }




    @RequiresApi(Build.VERSION_CODES.S)
    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val description = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager =
                context?.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}

