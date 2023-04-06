package com.example.weather.ui.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingBinding
import com.example.weather.ui.home.ui.HomeFragment
import com.example.weather.ui.main.Constants
import com.example.weather.ui.main.MainActivity
import java.util.*
import android.content.res.Configuration


class SettingFragment : Fragment() {
    private lateinit var languageRadioButton: RadioButton
    private lateinit var locationRadioButton: RadioButton
    private lateinit var tempRadioButton: RadioButton
    lateinit var sharedPreferences: SharedPreferences
    private var _binding: FragmentSettingBinding? = null
    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding = _binding!!
        val root: View = binding.root
        setCondithion()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!


        //Language
        _binding!!.languageRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            languageRadioButton = view.findViewById<View>(checkedId) as RadioButton
            when (languageRadioButton.text) {
                getString(R.string.arabic) -> {
                    sharedPreferences.edit()
                        .putString(Constants.lang, Constants.Enum_language.ar.toString()).commit()
                    setLan("ar")
                }
                getString(R.string.english) -> {
                    sharedPreferences.edit()
                        .putString(Constants.lang, Constants.Enum_language.en.toString()).commit()
                    setLan("an")
                }
            }

        }
        //location
        _binding!!.locationRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            locationRadioButton = view.findViewById<View>(checkedId) as RadioButton
            when (locationRadioButton.text.toString()) {
                getString(R.string.map) -> {
                    sharedPreferences.edit()
                        .putString(Constants.LOCATION, Constants.Enum_LOCATION.map.toString())
                        .commit()
                    val action =
                        SettingFragmentDirections.actionNavigationSettingToMapsFragment("Home")
                    Navigation.findNavController(requireView()).navigate(action)

                }
                getString(R.string.gps) -> {
                    sharedPreferences.edit()
                        .putString(Constants.LOCATION, Constants.Enum_LOCATION.gps.toString())
                        .commit()
                }

            }
        }
        //Temprature
        _binding!!.tempRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            tempRadioButton = view.findViewById<View>(checkedId) as RadioButton
            when (tempRadioButton.text.toString()) {
                getString(R.string.Celsusi) -> {
                    sharedPreferences.edit()
                        .putString(Constants.units, Constants.Enum_units.metric.toString()).commit()
                }
                getString(R.string.KELVIN) -> {
                    sharedPreferences.edit()
                        .putString(Constants.units, Constants.Enum_units.standard.toString())
                        .commit()
                }
                getString(R.string.Ferherhit) -> {
                    sharedPreferences.edit()
                        .putString(Constants.units, Constants.Enum_units.imperial.toString())
                        .commit()
                }
            }

        }

    }

    //Set_Language_Arabic/English
    private fun setLan(language: String) {
        var locale = Locale(language)
        Locale.setDefault(locale)
        var config = Configuration()
        config.setLocale(locale)
        context?.resources?.updateConfiguration(config,context?.resources?.displayMetrics)
        activity?.startActivity(Intent(context,MainActivity::class.java))
    }

    //If_Conditions
    fun setCondithion() {
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!

        val lang =
            sharedPreferences.getString(Constants.lang, Constants.Enum_language.en.toString())
        val loc =
            sharedPreferences.getString(Constants.LOCATION, Constants.Enum_LOCATION.gps.toString())
        val units =
            sharedPreferences.getString(Constants.units, Constants.Enum_units.metric.toString())
        if (lang == Constants.Enum_language.en.toString()) {
            _binding!!.languageRadioGroup.check(binding.englishRadioButton.id)
        } else {
            _binding!!.languageRadioGroup.check(binding.arabicRadioButton.id)
        }
        if (loc == Constants.Enum_LOCATION.map.toString()) {
            _binding!!.languageRadioGroup.check(binding.mapRadioButton.id)
        } else {
            _binding!!.languageRadioGroup.check(binding.gpsRadioButton.id)
        }
        if (units == Constants.Enum_units.standard.toString()) {
            _binding!!.languageRadioGroup.check(binding.kelvinRadioButton.id)
        }
        if (units == Constants.Enum_units.imperial.toString()) {
            _binding!!.languageRadioGroup.check(binding.fehrenheitRadioButton.id)
        }
        if (units == Constants.Enum_units.metric.toString()) {
            _binding!!.languageRadioGroup.check(binding.celsiusRadioButton.id)
        }
    }

}