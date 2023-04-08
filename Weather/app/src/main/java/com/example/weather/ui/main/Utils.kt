package com.example.weather.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.weather.R
import com.example.weather.ui.alert.ui.AlertFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

object Utils {
    fun getIconUrl(iconCode:String):String{
        return  "https://openweathermap.org/img/wn/" + iconCode + "@4x.png";
    }
    @SuppressLint("SimpleDateFormat")
    fun formatTime(dateObject: Long): String {
        val date = Date(dateObject * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
    fun formatTimeArabic(dateObject: Long): String {
        val date = Date(dateObject * 1000L)
        val sdf = SimpleDateFormat("HH:mm", Locale("ar"))
        return sdf.format(date)
    }
    @SuppressLint("SimpleDateFormat")
    fun formatDate(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)

    }
    @SuppressLint("SimpleDateFormat")
    fun formatDateAlert(dt:Long):String{
        val date= Date(dt)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)

    }
    @SuppressLint("SimpleDateFormat")
    fun formatTimeAlert(dt:Long):String{
        val date = Date(dt)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)

    }

    fun formatDateArabic(dt:Long):String {
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale("ar"))
        return sdf.format(date)
    }

    fun formatday(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("EEEE")
        return sdf.format(date)

    }
    fun formatdayArabic(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("EEEE", Locale("ar"))
        return sdf.format(date)

    }
    fun englishNumberToArabicNumber(number: String): String {
        val arabicNumber = mutableListOf<String>()
        for (element in number.toString()) {
            when (element) {
                '1' -> arabicNumber.add("١")
                '2' -> arabicNumber.add("٢")
                '3' -> arabicNumber.add("٣")
                '4' -> arabicNumber.add("٤")
                '5' -> arabicNumber.add("٥")
                '6' -> arabicNumber.add("٦")
                '7' -> arabicNumber.add("٧")
                '8' -> arabicNumber.add("٨")
                '9' -> arabicNumber.add("٩")
                '0' ->arabicNumber.add("٠")
                '.'->arabicNumber.add(".")
                '-'->arabicNumber.add("-")
                else -> arabicNumber.add(".")
            }
        }
        return arabicNumber.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
            .replace(" ", "")
    }
    fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String{
        var address:MutableList<Address>?=null
        val geocoder= Geocoder(context)
        address =geocoder.getFromLocation(lat!!,lon!!,1)
        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else if (address?.get(0)?.countryName.isNullOrEmpty())
        {
            return "Unkown Country"
        }
        else if (address?.get(0)?.adminArea.isNullOrEmpty())
        {
            return address?.get(0)?.countryName.toString()

        }        else
            return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
    fun getAddressArabic(context: Context, lat:Double, lon:Double):String{
        var address:MutableList<Address>?=null

        val geocoder= Geocoder(context, Locale("ar"))
        address =geocoder.getFromLocation(lat,lon,1)

        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else if (address?.get(0)?.countryName.isNullOrEmpty())
        {
            return "Unkown Country"
        }
        else if (address?.get(0)?.adminArea.isNullOrEmpty())
        {
            return address?.get(0)?.countryName.toString()

        }
        else
            return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea

    }
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(currentTime)
    }
    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): Pair<String,Long> {
        val calender=Calendar.getInstance()
        val currentTime = calender.time
        val sdf = SimpleDateFormat("HH:mm")
        val alert=calender.timeInMillis
        return Pair( sdf.format(currentTime),alert)

    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime2(): Pair<String,Long> {
        val calender=Calendar.getInstance()
        val currentTime = calender.time
        val sdf = SimpleDateFormat("HH:mm")
        val alert=calender.timeInMillis
        return Pair( sdf.format(currentTime),alert)

    }
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDatePlusOne(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(tomorrow)
    }
    @SuppressLint("SimpleDateFormat")
    fun pickedDateFormatDate(dt: Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(dt)
    }
    @SuppressLint("SimpleDateFormat")
    fun pickedDateFormatTime(dt: Date): String {
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(dt)
    }

    fun generateRandomNumber():Int{
        return Random.nextInt()
    }

    fun canelAlarm(context: Context, alert:String?, requestCode:Int) {
        var alarmMgr: AlarmManager? = null
        lateinit var alarmIntent: PendingIntent

        alarmMgr = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context.applicationContext, AlertFragment::class.java).putExtra(
            Constants.Alert,alert).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext, requestCode, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        alarmMgr.cancel(alarmIntent)

    }
    fun isDaily(startTime: Long,endTime:Long):Boolean{
        return endTime-startTime >= 86400000
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
    fun setLanguageEnglish(activity: Activity) {
        val languageToLoad = "en"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity.baseContext.getResources().updateConfiguration(
            config,
            activity.baseContext.getResources().getDisplayMetrics()
        )
    }
    fun setLanguageArabic(activity: Activity) {
        val languageToLoad = "ar"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        activity.baseContext.getResources().updateConfiguration(
            config,
            activity.baseContext.getResources().getDisplayMetrics()
        )

    }
    fun updateResources(context: Context, language: String): Boolean {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.getResources()
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return true
    }
    fun setAppLocale(localeCode: String,context: Context) {
        val resources = context.resources
        val dm = resources.displayMetrics
        val config: Configuration = resources.configuration
        config.setLocale(Locale(localeCode.lowercase(Locale.getDefault())))
        resources.updateConfiguration(config, dm)
    }

    fun changeLang(context: Context, lang_code: String): ContextWrapper? {
        var context: Context = context
        val sysLocale: Locale
        val rs: Resources = context.getResources()
        val config: Configuration = rs.getConfiguration()
        sysLocale =
            config.getLocales().get(0)
        if (lang_code != "" && !sysLocale.getLanguage().equals(lang_code)) {
            val locale = Locale(lang_code)
            Locale.setDefault(locale)
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        }
        return ContextWrapper(context)
    }



    fun getAddress(latitude: Double, longitude: Double, lang:String, context: Context): String {
        var address = ""
        val geocoder = Geocoder(context,  Locale(lang))
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null && addresses.size > 0) {
            if(addresses.get(0).adminArea!=null) {
                address = addresses.get(0).adminArea
            }
        }
        return address
    }



    fun getCurrentTemperature(context: Context): String {
        val sharedPreference =  context.getSharedPreferences("My Shared", Context.MODE_PRIVATE)
        sharedPreference.getString(Constants.units,Constants.Enum_units.standard.toString())
        return when (  sharedPreference.getString(Constants.units,Constants.Enum_units.standard.toString())) {
            Constants.Enum_units.metric.toString() -> {
                context.getString(R.string.Celsusi)
            }
            Constants.Enum_units.imperial.toString()-> {
                context.getString(R.string.Ferherhit)
            }
            Constants.Enum_units.standard.toString() -> {
                context.getString(R.string.KELVIN)
            }
            else -> {
                context.getString(R.string.Celsusi)
            }
        }
    }


    fun getCurrentSpeed(context: Context): String {
        val sharedPreference =  context.getSharedPreferences("My Shared", Context.MODE_PRIVATE)
        return when (  sharedPreference.getString(Constants.units,Constants.Enum_units.standard.toString())) {
            Constants.Enum_units.metric.toString() -> {
                context.getString(R.string.MeterPerSecond)
            }
            Constants.Enum_units.imperial.toString()-> {
                context.getString(R.string.MiliPerHour)
            }
            Constants.Enum_units.standard.toString() -> {
                context.getString(R.string.MeterPerSecond)
            }
            else -> {
                context.getString(R.string.MeterPerSecond)
            }
        }
    }


  /*  @SuppressLint("SuspiciousIndentation")
    fun setBackgroundContainer(timeState: String, context: Context) :Int{
        val sharedPreference =  context.getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        val iconValue: Int

        when (timeState) {
            "02n" ->{
                iconValue =     R.drawable.background_dawn
                sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "01n" ->{
                iconValue =     R.drawable.background_night
                sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "01d"-> {
                iconValue =    R.drawable.background_morning
                sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "04d" ->{
                iconValue =    R.drawable.background_evening
                sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "13n" ->{
                iconValue =    R.drawable.background_evening
                sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "13d" ->{
                iconValue =    R.drawable.background_evening
                sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "10d" ->{
                iconValue =   R.drawable.background_noon
                sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            else -> {
                iconValue = R.drawable.background_evening
                sharedPreference.edit().putInt(CONST.Background, iconValue).commit()
            }

        }

        return iconValue
    } */

   /* @SuppressLint("SuspiciousIndentation")
    fun setLotte(timeState: String, context: Context) :Int{
        val sharedPreference =  context.getSharedPreferences("getSharedPreferences", Context.MODE_PRIVATE)
        val iconValue: Int

        when (timeState) {
            "13d" ->{
                iconValue =     R.raw.snow
                //  sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "13n" ->{
                iconValue =     R.raw.snow
                //  sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "09d" ->{
                iconValue =     R.raw.rain
                //  sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "10d" ->{
                iconValue =     R.raw.rain
                //  sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "09n" ->{
                iconValue =     R.raw.rain
                //  sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }
            "10n" ->{
                iconValue =     R.raw.rain
                //  sharedPreference.edit().putInt(CONST.Background,iconValue).commit()
            }

            else -> {
                iconValue = 0
                //    sharedPreference.edit().putInt(CONST.Background, iconValue).commit()
            }

        }

        return iconValue
    } */


    fun convertStringToArabic(value: String): String {
        return (value + "")
            .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
            .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
            .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
            .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
            .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
    }
    // convert to hours
    @SuppressLint("SimpleDateFormat")
    fun convertToTime(dt: Long,lang :String): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("h:mm a", Locale(lang))
        return format.format(date)
    }



    fun convertToDay(dt: Long,lang :String): String? {
        val date = Date(dt * 1000)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val en = Locale.ENGLISH
        val ar = Locale("ar", "SA")
        var location:Locale
        if (lang.equals("ar")) {
            location = ar
        }else{
            location =en
        }
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,location )
    }

    fun progressDialog(context: Context): ProgressDialog {
        var progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)
        return progressDialog
    }


    fun convertToDate(dt: Long,lang :String): String {
        val date = Date(dt * 1000)
        val format = SimpleDateFormat("d MMM, yyyy", Locale(lang))
        return format.format(date)
    }

    fun getseconds(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.timeInMillis / 1000
    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }






}

