package com.example.weather.ui.alert.recevier

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch


class AlertRecevier : BroadcastReceiver() {
    lateinit var notificationManager: NotificationManager
    var notificationId: Int? = null
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        lateinit var notification: Uri
        lateinit var alarm: Ringtone
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        sharedPreferences = context.getSharedPreferences("My Shared", MODE_PRIVATE)
        val repo = Reposatory.getInstance(
            Client.getInstance(), ConcreteLocalSource(context),
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        var alertSettings = repo.getAlertSettings()
        val alertJson = intent.getStringExtra(Constants.Alert)
        var alert = Gson().fromJson(alertJson, Alert::class.java)
        val notificationHelper = Notification(context)
        notificationId = 1

            notificationManager = notificationHelper.alarmNotificationManager(context)

        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            if (!Utils.isDaily(alert.startDay, alert.endDay)) {
                Utils.canelAlarm(context, alert.toString(), alert.startDay.toInt())
                repo.deleteAlert(alert)
                WorkManager.getInstance(context.applicationContext)
                    .cancelAllWorkByTag(alert.startDay.toString())
            }
            try {
                repo.getWeatherOverNetwork(
                    lat = alert.lat,
                    lon = alert.lon,
                    "exclude",
                    "3b2709ccc4bdcdaac7f7ea5c91b3da94",
                    "en",
                    "unit"
                ).collectLatest {
                    val bitmap = arrayOf<Bitmap?>(null)
                    Glide.with(context)
                        .asBitmap()
                        .load(Utils.getIconUrl(it.body()?.current!!.weather[0].icon))
                        .into(object : CustomTarget<Bitmap?>() {
                            @RequiresApi(Build.VERSION_CODES.S)
                            override fun onResourceReady(
                                resource: Bitmap,
                                @Nullable transition: Transition<in Bitmap?>?
                            ) {
                                bitmap[0] = resource
                                notification = RingtoneManager.getActualDefaultRingtoneUri(
                                    context.applicationContext,
                                    RingtoneManager.TYPE_ALARM
                                )
                                alarm = RingtoneManager.getRingtone(
                                    context.applicationContext,
                                    notification
                                )
                                if (alertSettings?.isALarm == true && !alertSettings.isNotification) {
                                    alarm.play()
                                    notificationManager.notify(
                                        notificationId!!, notificationHelper.getNotification(
                                            context,
                                            notificationId!!,
                                            Utils.getAddressEnglish(
                                                context,
                                                alert.lat,
                                                alert.lon
                                            ),
                                            it.body()!!.current.weather[0].description,
                                            bitmap[0]!!
                                        )
                                    )

                                }
                                if (alertSettings?.isALarm == false && alertSettings.isNotification) {
                                    notificationManager.notify(
                                        notificationId!!, notificationHelper.getNotificationBuilder(
                                            Utils.getAddressEnglish(
                                                context,
                                                alert.lat,
                                                alert.lon
                                            )!!,
                                            it.body()!!.current.weather[0].description,
                                            context,
                                            bitmap[0]!!

                                        ).build()
                                    )
                                }
                            }

                            override fun onLoadCleared(@Nullable placeholder: Drawable?) {

                            }
                        })
                }
            } finally {
                cancel()
            }

        }


    }


}








