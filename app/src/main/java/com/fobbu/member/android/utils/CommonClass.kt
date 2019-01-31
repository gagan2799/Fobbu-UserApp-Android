package com.fobbu.member.android.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginSignupModule.LoginActivity
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.backgroundServices.FetchStatusAPI
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.option_menu_layout.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNREACHABLE_CODE")
/**
 * Created by abc on 12/1/18.
 */
class CommonClass(activity1: Activity, context1: Context) {
    private var activity: Activity = activity1

    private var context: Context = context1

    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    private lateinit var myCalendar: Calendar

    private var startDateCustom = ""

    private var endDateCustom = ""

    var aPI_KEY: String = "Fobbu"

    fun putString(preference: String, value: String) {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val prefsEditor = myPrefs.edit()

        prefsEditor.putString(preference, value).apply()
    }

    fun removeString(KEY: String) {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)
        val prefsEditor = myPrefs.edit()
        prefsEditor.remove(KEY)
        prefsEditor.apply()
    }

    fun getString(preference: String): String {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        return myPrefs.getString(preference, "")
    }

    fun putStringList(preference: String, value: ArrayList<HashMap<String, Any>>) {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val prefsEditor = myPrefs.edit()

        val gson = Gson()

        val json: String = gson.toJson(value)

        prefsEditor.putString(preference, json)

        prefsEditor.apply()

    }

    fun getStringList(preference: String): ArrayList<HashMap<String, Any>> {

        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val gson = Gson()

        val json: String = myPrefs.getString(preference, "")

        val type = object : TypeToken<ArrayList<HashMap<String, Any>>>() {}.type

        val list: ArrayList<HashMap<String, Any>> = gson.fromJson(json, type)

        println("LIst $list")

        return list
    }


    fun errorMessage(response: String): String {
        try {
            val jsonObject = JSONObject(response)

            return jsonObject.getString("message")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun isStringEmpty(text: String): Boolean {
        return text.isEmpty()
    }

    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun workDoneReviewSend() {
        val myPrefs: SharedPreferences = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor

        prefsEditor = myPrefs.edit()
        prefsEditor.remove(RsaConstants.ServiceSaved.fobbuRequestId)
        prefsEditor.putString(RsaConstants.RsaTypes.onGoingRsaScreen, "")
        prefsEditor.putString(RsaConstants.RsaTypes.onGoingRsaScreenType, "")
        prefsEditor.putString(RsaConstants.RsaTypes.checkIfOnGoingRsaRequest,"")
        prefsEditor.putString(RsaConstants.RsaTypes.checkStatus,"")

        prefsEditor.apply()

        try {
            activity.stopService(Intent(activity,FetchStatusAPI::class.java))
        }
        catch (e:java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun clearPreference() {
        val myPrefs: SharedPreferences = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val prefsEditor: SharedPreferences.Editor

        prefsEditor = myPrefs.edit()

        val number = myPrefs.getString("Local_Number", "")

        val pin = myPrefs.getString("Local_Pin", "")

        prefsEditor.clear()

        prefsEditor.apply()

        prefsEditor.putString("Local_Number", number).apply()

        prefsEditor.putString("Local_Pin", pin).apply()

        prefsEditor.putString("CoachMark_first_time", "true").commit()

        try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nm.cancelAll()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val i = Intent(activity, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(i)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.finishAffinity()
        } else {
            ActivityCompat.finishAffinity(activity)
        }
    }

    fun checkInternetConn(con: Context): Boolean {
        try {
            val connMgr = con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            return mobile.isConnected || wifi.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }


    fun internetIssue(context: Context) {

        val alertDialog = android.app.AlertDialog.Builder(context).create()
        alertDialog.setTitle(context.resources.getString(R.string.networkIssue))
        alertDialog.setMessage(context.resources.getString(R.string.noInternet))
        alertDialog.setButton(
            android.app.AlertDialog.BUTTON_NEGATIVE,
            context.resources.getString(R.string.setting)
        ) { dialogInterface, i ->
            alertDialog.dismiss()

            context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
        }
        alertDialog.setButton(
            android.app.AlertDialog.BUTTON_POSITIVE,
            context.resources.getString(R.string.cancelled)
        ) { _, _ ->
            alertDialog.dismiss()
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }


    @SuppressLint("SimpleDateFormat")
    fun getTimerTime(expired: String): Boolean {

        try {
            // String inputText = "Tue May 21 14:32:00 GMT 2012";

            val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            // inputFormat.timeZone = TimeZone.getTimeZone("GMT")

            // Adjust locale and zone appropriately
            val expire = inputFormat.parse(expired)

            val currentDate = getInstance().time
            if (currentDate.after(expire)) { // expiry date is either equal to or before current time

                return false
            } else {
                // expiry time is greater than current time
                val difference = expire.time - currentDate.time
                if (difference >= 1 * 1 * 60 * 1000) {
                    // difference is less than 1 minute
                    return true
                }
            }

        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return false
        }

        return false
    }

    @SuppressLint("SimpleDateFormat")
    fun compareTwoDates(start: String, end: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val strStartDate = sdf.parse(start)

        val strEndDate = sdf.parse(end)

        if (strEndDate.after(strStartDate)) {
            return true
        }
        return false
    }

    @SuppressLint("SimpleDateFormat")
    fun getDesireFormat(input: String, output: String, dateString: String): String {
        val inputFormat = SimpleDateFormat(input)
        val outputFormat = SimpleDateFormat(output)
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun compareDates(date: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val strDate = sdf.parse(date)
        if (Date().after(strDate)) {
            return true
        }
        return false
    }

    fun giveDynamicHeightRelativeGallery(): RelativeLayout.LayoutParams? {

        val parms: RelativeLayout.LayoutParams
        try {
            val display = activity.windowManager.defaultDisplay
            val width = display.width // ((display.getWidth()*20)/100)
            var height = display.height// ((display.getHeight()*30)/100)

            val layoutWidth = width / 3
            val layoutHeight = width / 3

            parms = RelativeLayout.LayoutParams(layoutWidth, RelativeLayout.LayoutParams.WRAP_CONTENT)

            return parms
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @SuppressLint("MissingPermission")
    fun callOnPhone(number: String) {

        try {
            val intent = Intent(Intent.ACTION_CALL)

            intent.data = Uri.parse("tel:$number")
            activity.startActivity(intent)
        }
        catch (e:java.lang.Exception)
        {
            e.printStackTrace()
        }

    }

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }


    fun openDatePicker(activity:Activity,from: String, textview: TextView) {

        myCalendar = Calendar.getInstance()

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateLabel(from, textview)
        }

        val datePicker = DatePickerDialog(activity, R.style.CustomPickerTheme, datePickerDialog, myCalendar
            .get(YEAR), myCalendar.get(MONTH),
            myCalendar.get(DAY_OF_MONTH))
        //datePicker.datePicker.minDate = myCalendar.timeInMillis
        //datePicker.datePicker.maxDate = myCalendar.timeInMillis + 2592000000
        datePicker.show()

    }


    @SuppressLint("SimpleDateFormat")
    private fun updateLabel(from: String, etDate: TextView?) {
        val myFormat = "dd MMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)

        etDate!!.text = sdf.format(myCalendar.time)

        val sdfServer = SimpleDateFormat("yyyy-MM-dd")
        // sdf.timeZone = TimeZone.getTimeZone("UTC")

        if (from == "Start") {
            startDateCustom = sdfServer.format(myCalendar.time)
        } else
            endDateCustom = sdfServer.format(myCalendar.time)

        /*  rbLastMonth.isChecked = false
          rbLastWeek.isChecked = false*/
    }

    fun showDailog(activity: Activity)
    {
        val builder= AlertDialog.Builder(activity)

        builder.setTitle("Delete")

        builder.setMessage("Do you want to delete this contact from the list?")
    }


    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(today:String):String
    {
        val c:Date = getInstance().time
        System.out.println("Current time => $c")

        val df:SimpleDateFormat  =  SimpleDateFormat("dd MMM")

        df.format(c)

        return   df.format(c).toString()
       /* else
        {
            val date = df.parse(df.format(c).toString())

            val cal = Calendar.getInstance()

            cal.time=date

            cal.add(Calendar.DATE, -1)

            df.format(cal.time)

            df.toString()
        }*/
    }


    // Method for setting  Animation
    fun setAnimation(customAnimation: Int, view: View, activity: Activity)
    {
        val animation: Animation = AnimationUtils.loadAnimation(activity, customAnimation)

        view.startAnimation(animation)
    }

      fun isAppIsInBackground(): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo.packageName == context.packageName) {
                isInBackground = false
            }
        }

        return isInBackground
    }

}
