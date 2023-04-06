package com.example.weather.ui.alert.recevier

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.work.WorkManager
import com.example.mvvm.Model.Reposatory
import com.example.weather.ui.main.Utils
import com.example.weather.data.weather.LocalSource.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.models.Alert
import com.example.weather.ui.alert.notification.Notification
import com.example.weather.ui.main.Constants
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlertRecevier : BroadcastReceiver() {
    lateinit var notificationManager: NotificationManager
    var notificationId: Int? = null
    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        lateinit var notification: Uri
        lateinit var alarm: Ringtone
    }
    override fun onReceive(context: Context, intent: Intent) {
        sharedPreferences = context.getSharedPreferences("My Shared", MODE_PRIVATE)
        val repo = Reposatory.getInstance(Client.getInstance(), ConcreteLocalSource(context),
            PreferenceManager.getDefaultSharedPreferences(context))
        val alertJson = intent.getStringExtra(Constants.Alert)
        var alert = Gson().fromJson(alertJson, Alert::class.java)
        val notificationHelper = Notification(context)
        notificationId = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationManager = notificationHelper.alarmNotificationManager(context)
        }
        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            Utils.canelAlarm(context, alert.toString(), alert.startDay.toInt())
            repo.deleteAlert(alert)
            WorkManager.getInstance(context.applicationContext)
                .cancelAllWorkByTag(alert.startDay.toString())
            repo.getWeatherOverNetwork(lat = alert.lat, lon = alert.lon,"exclude","3b2709ccc4bdcdaac7f7ea5c91b3da94","en","unit" )
            notification = RingtoneManager.getActualDefaultRingtoneUri(
                context.applicationContext,
                RingtoneManager.TYPE_ALARM
            )
            alarm = RingtoneManager.getRingtone(
                context.applicationContext,
                notification
            )
        }

    }
}








