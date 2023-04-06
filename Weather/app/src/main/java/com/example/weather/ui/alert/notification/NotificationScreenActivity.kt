package com.example.weather.ui.alert.notification

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.weather.databinding.ActivityNotificationScreenBinding


class NotificationScreenActivity : AppCompatActivity() {
    var notification = com.example.weather.ui.alert.recevier.AlertRecevier.alarm
    private var _binding: ActivityNotificationScreenBinding? = null
    private val binding get() = _binding
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotificationScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        val notificationHelper = Notification(applicationContext)
        binding?.btnDismiss?.setOnClickListener {
            notification.stop()
            notificationHelper.alarmNotificationManager(applicationContext).cancel(1)
            finish()
        }
    }
}