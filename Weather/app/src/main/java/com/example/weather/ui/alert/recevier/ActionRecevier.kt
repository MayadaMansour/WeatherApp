package com.example.weather.ui.alert.recevier
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.ui.alert.notification.Notification
import com.example.weather.ui.main.Constants


class ActionRecevier:BroadcastReceiver() {
    lateinit var  alarm: Ringtone
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val notification= Notification(context)
        if (intent.action.equals(Constants.ACTION_SNOOZE)) {
            alarm.stop()
            notification.alarmNotificationManager(context).cancel(1)
        }

    }
}