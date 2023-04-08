package com.example.weather.ui.alert.notification

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.weather.R
import java.util.*
import androidx.work.*
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.LocalSource.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentNotificationAlertBinding
import com.example.weather.models.Alert
import com.example.weather.models.AlertSettings
import com.example.weather.ui.alert.recevier.doWorker
import com.example.weather.ui.alert.view.AlertViewModel
import com.example.weather.ui.alert.view.AlertViewModelFactory
import com.example.weather.ui.main.Constants
import com.example.weather.ui.main.Utils
import com.example.weather.ui.main.Utils.getCurrentDate
import com.example.weather.ui.main.Utils.getCurrentTime
import com.example.weather.ui.main.Utils.getCurrentTime2
import com.google.gson.Gson
import java.util.concurrent.TimeUnit


class NotificationAlertFragment : DialogFragment() {

    private var _binding: FragmentNotificationAlertBinding? = null
    private val binding get() = _binding!!
    lateinit var start_cal: Calendar
    lateinit var end_cal: Calendar
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var alertViewModel: AlertViewModel
    private lateinit var editor: SharedPreferences.Editor
    val ALERTSETTINGS = "ALERTSETTINGS"
    lateinit var alert: Alert

    companion object {
        const val TAG = "DialogNotification"
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window!!.attributes
        dialog!!.window!!.setBackgroundDrawableResource(R.color.light_gray)
        window.gravity = Gravity.BOTTOM
        dialog!!.window!!.attributes = window
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getDialog()?.requestWindowFeature(STYLE_NO_TITLE)
        setCancelable(true)
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        alert = Alert(getCurrentTime().second, getCurrentTime2().second, 0.0, 0.0, "City")
        val repo = Reposatory.getInstance(
            Client.getInstance(), ConcreteLocalSource(requireActivity()),
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        alertViewModel = ViewModelProvider(
            this.requireActivity(),
            AlertViewModelFactory(repo)
        )[AlertViewModel::class.java]
        val inputData = Data.Builder()
        inputData.putString(Constants.Alert, Gson().toJson(alert).toString())
        val alarm = alertViewModel.getAlertSettings()
        start_cal = Calendar.getInstance()
        end_cal = Calendar.getInstance()
        binding.fromDate.text = getCurrentDate()
        alert.startDay = Calendar.getInstance().timeInMillis
        alert.endDay = Calendar.getInstance().timeInMillis
        binding.toDate.text = getCurrentDate()
        binding.fromTime.text = getCurrentTime().first
        binding.toTime.text = getCurrentTime2().first
        if (Utils.isOnline(requireContext())) {
            binding.btnCountry.isEnabled = true
            binding.btnKo.isEnabled = true
        } else {
            binding.btnCountry.isEnabled = false
            binding.btnKo.isEnabled = false
            Toast.makeText(requireContext(), "No Connection ", Toast.LENGTH_SHORT).show()
        }
        if (alarm?.isALarm == true && !alarm.isNotification) {
            binding.radioButtonAlarm.isChecked = true
        }
        if (alarm?.isALarm == false && alarm.isNotification) {
            binding.radioButtonNotify.isChecked = true
        }

        binding.fromTime.setOnClickListener {
            pickDateTime(binding.fromDate, binding.fromTime, start_cal)
        }
        binding.toTime.setOnClickListener {
            pickDateTime(binding.toDate, binding.toTime, end_cal)
        }
        binding.btnCountry.text =
            Utils.getAddressEnglish(requireContext(), alarm?.lat, alarm?.lon)
        ////////////////Country_Button//////////
        binding.btnCountry.setOnClickListener {
            val action =
                NotificationAlertFragmentDirections.actionNotificationAlertFragmentToMapsFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
        ////////////////Ok_Button///////////////
        binding.btnKo.setOnClickListener {
            var alert = Alert(
                startDay = start_cal.timeInMillis,
                endDay = end_cal.timeInMillis,
                lat = alarm!!.lat,
                lon = alarm.lon,
                AlertCityName = Utils.getAddressEnglish(requireContext(), alarm!!.lat, alarm.lon)
            )


            if (alert.startDay < alert.endDay) {
                if (binding.radioButtonAlarm.isChecked) {
                    alarm?.isALarm = true
                    alarm?.isNotification = false
                }
                if (binding.radioButtonNotify.isChecked) {
                    alarm?.isALarm = false
                    alarm?.isNotification = true
                }
                alarm?.let { alertViewModel.saveAlertSettings(it) }
                alertViewModel.insertAlertDB(alert)


                val inputData = Data.Builder()
                inputData.putString(Constants.Alert, Gson().toJson(alert).toString())

                val myConstraints: Constraints = Constraints.Builder()
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                Toast.makeText(context, "Daily", Toast.LENGTH_SHORT).show()
                val myWorkRequest =
                    PeriodicWorkRequestBuilder<doWorker>(1, TimeUnit.DAYS).setConstraints(
                        myConstraints
                    ).setInputData(inputData.build()).addTag(alert.startDay.toString()).build()
                WorkManager.getInstance(requireContext().applicationContext)
                    .enqueueUniquePeriodicWork(
                        alert.startDay.toString(),
                        ExistingPeriodicWorkPolicy.REPLACE,
                        myWorkRequest
                    )
                dismiss()
            } else {

                Toast.makeText(
                    context,
                    "Please specify the end time of your alert",
                    Toast.LENGTH_SHORT
                ).show()
            }

            //////////////////Cancel_Button///////////////////

        }

        binding.btnCancel.setOnClickListener {
            Log.i("TAG", "getLocalWeathers: errror")
            dismiss()
        }
        return root
    }

    ///////////////////////////////////////////Methods//////////////////////////////////////
    private fun pickDateTime(tvdate: TextView, tvTime: TextView, calendar: Calendar) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        var pickedDateTime: Calendar
        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(year, month, day, hour, minute)
                        tvdate.text = Utils.pickedDateFormatDate(pickedDateTime.time)
                        tvTime.text = Utils.pickedDateFormatTime(pickedDateTime.time)
                        calendar.time = pickedDateTime.time

                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }

    //////////////////////////////////////////////////methods
    fun saveAlertSettings(alertSettings: AlertSettings) {
        editor = sharedPreferences.edit()
        editor.putString(ALERTSETTINGS, Gson().toJson(alertSettings))
        editor.commit()
    }

    fun getAlertSettings(): AlertSettings? {
        val settingsStr = sharedPreferences.getString(ALERTSETTINGS, null)
        var alertSettings: AlertSettings? = AlertSettings()
        if (settingsStr != null) {
            alertSettings = Gson().fromJson(settingsStr, AlertSettings::class.java)

        }
        return alertSettings
    }


}
