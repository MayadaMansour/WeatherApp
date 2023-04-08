package com.example.weather.ui.alert.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.LocalSource.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.data.weather.netwok.LocalDataStateAlerts
import com.example.weather.databinding.FragmentAlertBinding
import com.example.weather.ui.alert.notification.NotificationAlertFragment
import com.example.weather.ui.alert.recevier.AlertRecevier
import com.example.weather.ui.alert.view.AlertViewModel
import com.example.weather.ui.alert.view.AlertViewModelFactory
import com.example.weather.ui.main.Constants
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest


class AlertFragment : Fragment() {
    lateinit var dialog: ProgressDialog
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var alertViewModel: AlertViewModel
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
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        val repo = Reposatory.getInstance(
            Client.getInstance(), ConcreteLocalSource(requireActivity()),
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        alertViewModel =
            ViewModelProvider(
                this.requireActivity(),
                AlertViewModelFactory(repo)
            )[AlertViewModel::class.java]
        alertViewModel.getAlertsDB()
        dialog = progressDialog(requireContext())
        binding.btnAlerts.setOnClickListener {
            NotificationAlertFragment().show(childFragmentManager, NotificationAlertFragment.TAG)
            lifecycleScope.launch {
                alertViewModel.currentAlert.collectLatest {
                    when (it) {
                        is LocalDataStateAlerts.Loading -> {
                        }
                        is LocalDataStateAlerts.Fail -> {
                            Toast.makeText(
                                requireContext(),
                                "No Connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is LocalDataStateAlerts.Success -> {
                                binding.rvAlerts.apply {
                                    adapter = AlertAdapter(it.data, requireContext()) {
                                        canelAlarm(
                                            requireContext(),
                                            it.toString(),
                                            it.startDay.toInt()
                                        )
                                        alertViewModel.deleteAlertDB(it)
                                        WorkManager.getInstance(context.applicationContext)
                                            .cancelAllWorkByTag(it.startDay.toString())
                                    }
                                    layoutManager = LinearLayoutManager(requireContext())
                                }
                        }
                        else -> {}
                    }
                }
            }
        }
        return root
    }

    //////////////////////////////////////////////Methods//////////////////////////////////
    private fun progressDialog(context: Context): ProgressDialog {
        var progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)
        return progressDialog
    }

    @RequiresApi(Build.VERSION_CODES.M)
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





