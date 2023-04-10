package com.example.weather.ui.alert.recevier

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.work.WorkerParameters
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.LocalSource.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.models.Alert
import com.example.weather.ui.main.Constants
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class doWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var alert: Alert


    override suspend fun doWork(): Result {
        sharedPreferences =
            applicationContext.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        alert = Alert(22, 22, 22.2, 22.2, "City")
        val repo = Reposatory.getInstance(
            Client.getInstance(),
            ConcreteLocalSource(applicationContext),
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        val alertJson = inputData.getString(Constants.Alert)
        var alert = Gson().fromJson(alertJson, Alert::class.java)
        //val alertJson = inputData.getString(Constants.Alert)
        if (alert.endDay in alert.startDay..alert.endDay) {
            setAlarm(alert.startDay, alertJson, alert.endDay.toInt())
        }
        if (alert.endDay < System.currentTimeMillis()) {
            WorkManager.getInstance(applicationContext)
                .cancelAllWorkByTag(alert.startDay.toString())
            repo.deleteAlert(alert)
            canelAlarm(applicationContext, alert.toString(),alert.startDay.toInt())
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "your worker ended", Toast.LENGTH_SHORT).show()
            }
        }
        return Result.success()
    }

    //////////////////////////////////Functions//////////////////////////////////////////////////////
    private fun setAlarm(dateInMillis: Long, alert: String?, requestCode: Int) {
        var alarmMsg: AlarmManager? = null
        lateinit var intent: PendingIntent
        alarmMsg = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intent =
            Intent(applicationContext, AlertRecevier::class.java).putExtra(Constants.Alert, alert)
                .let { intent ->
                    PendingIntent.getBroadcast(
                        applicationContext, requestCode, intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }
        alarmMsg.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateInMillis,
            intent
        )
    }

    fun canelAlarm(context: Context, alert: String?, requestCode: Int) {
        var alarmMgr: AlarmManager? = null
        lateinit var alarmIntent: PendingIntent
        alarmMgr =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context.applicationContext, AlertRecevier::class.java).putExtra(
            Constants.Alert, alert
        ).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext, requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmMgr.cancel(alarmIntent)

    }

}