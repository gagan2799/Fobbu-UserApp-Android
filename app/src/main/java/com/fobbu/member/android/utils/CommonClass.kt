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
import android.support.v4.app.ActivityCompat
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginSignupModule.LoginActivity
import com.fobbu.member.android.backgroundServices.FetchStatusAPI
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNREACHABLE_CODE",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)
/**
 * Created by abc on 12/1/18.
 */
class CommonClass(activity1: Activity, context1: Context)
{
    private var activity: Activity = activity1

    private var context: Context = context1

    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    private lateinit var myCalendar: Calendar

    private var startDateCustom = ""

    private var endDateCustom = ""

    // function for validating pan card
    fun validatePan(pan:String):Boolean
    {
        val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")

        val matcher = pattern.matcher(pan)
// Check if pattern matches
        return matcher.matches()
    }

    // function for showing Sharing bottom sheet
    fun shareIt(context:Context)
    {
        val intent: Intent = Intent(android.content.Intent.ACTION_SEND)

        intent.type = "text/plain"

        val shareBody: String = "Here is the share content body"

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")

        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)

        context.startActivity(Intent.createChooser(intent, "Share via"))
    }

    // function for putting string in preference
    fun putString(preference: String, value: String)
    {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val prefsEditor = myPrefs.edit()

        prefsEditor.putString(preference, value).apply()
    }

    // function for removing string in preference
    fun removeString(KEY: String)
    {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val prefsEditor = myPrefs.edit()

        prefsEditor.remove(KEY)

        prefsEditor.apply()
    }

    // function for getting string in preference
    fun getString(preference: String): String
    {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        return myPrefs.getString(preference, "")
    }

    // function for putting string List in preference
    fun putStringList(preference: String, value: ArrayList<HashMap<String, Any>>)
    {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val prefsEditor = myPrefs.edit()

        val gson = Gson()

        val json: String = gson.toJson(value)

        prefsEditor.putString(preference, json)

        prefsEditor.apply()
    }

    private val emptyList= ArrayList<HashMap<String, Any>>()

    // function for removing List in preference
    fun getStringList(key: String): ArrayList<HashMap<String, Any>>
    {
        val myPrefs = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val gson = Gson()

        val json = myPrefs.getString(key, "")

        val type= object : TypeToken<java.util.ArrayList<java.util.HashMap<String, Any>>>(){}.type

        return if (json!="")
            gson.fromJson(json,type)

        else
            emptyList
    }

    // function for hiding soft keyboard
    fun hideSoftKeyboard(activity: Activity )
    {
        try
        {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
        }
        catch (e:java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    // function for showing Toast
    fun showToast(text: String)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    // function for clearing request
    fun workDoneReviewSend()
    {
        val myPrefs: SharedPreferences = context.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        val prefsEditor: SharedPreferences.Editor

        prefsEditor = myPrefs.edit()

        prefsEditor.remove(RsaConstants.ServiceSaved.fobbuRequestId)

        prefsEditor.putString(RsaConstants.RsaTypes.onGoingRsaScreen, "")

        prefsEditor.putString(RsaConstants.RsaTypes.onGoingRsaScreenType, "")

        prefsEditor.putString(RsaConstants.RsaTypes.checkIfOnGoingRsaRequest,"")

        prefsEditor.putString(RsaConstants.RsaTypes.checkStatus,"")

        prefsEditor.putString(RsaConstants.ServiceSaved.isNew,"")

        prefsEditor.apply()

        try
        {
            activity.stopService(Intent(activity,FetchStatusAPI::class.java))
        }
        catch (e:java.lang.Exception)
        {
            e.printStackTrace()
        }

        cancelNotification()
    }

    // function for cancelling notification
     fun cancelNotification()
    { try
        {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nm.cancelAll()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    // function for clearing preference
    @SuppressLint("ObsoleteSdkInt")
    fun clearPreference()
    {
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

        cancelNotification()

        val i = Intent(activity, LoginActivity::class.java)

        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        activity.startActivity(i)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            activity.finishAffinity()

        else
            ActivityCompat.finishAffinity(activity)
    }

    // function for checking internet connection
    fun checkInternetConn(con: Context): Boolean
    {
        try
        {
            val connMgr = con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            return mobile.isConnected || wifi.isConnected
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        return false
    }

    // function for showing custom dialog of internet issue
    fun internetIssue(context: Context)
    {
        val alertDialog = android.app.AlertDialog.Builder(context).create()

        alertDialog.setTitle(context.resources.getString(R.string.networkIssue))

        alertDialog.setMessage(context.resources.getString(R.string.noInternet))

        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, context.resources.getString(R.string.setting)) { _, i ->
            alertDialog.dismiss()

            context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
        }

        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, context.resources.getString(R.string.cancelled)) { _, _ ->
            alertDialog.dismiss()
        }

        alertDialog.setOnShowListener {
            alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)

            alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }
        alertDialog.show()
    }

    // function for converting time into desired format
    @SuppressLint("SimpleDateFormat")
    fun getDesireFormat(input: String, output: String, dateString: String): String
    {
        val inputFormat = SimpleDateFormat(input)

        val outputFormat = SimpleDateFormat(output)

        val date = inputFormat.parse(dateString)

        return outputFormat.format(date)
    }


    // function for giving dynamic height relative gallery
    fun giveDynamicHeightRelativeGallery(): RelativeLayout.LayoutParams?
    {
        val parms: RelativeLayout.LayoutParams

        try
        {
            val display = activity.windowManager.defaultDisplay

            val width = display.width // ((display.getWidth()*20)/100)

            var height = display.height// ((display.getHeight()*30)/100)

            val layoutWidth = width / 3

            val layoutHeight = width / 3

            parms = RelativeLayout.LayoutParams(layoutWidth, RelativeLayout.LayoutParams.WRAP_CONTENT)

            return parms
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return null
    }

    // function for calling
    @SuppressLint("MissingPermission")
    fun callOnPhone(number: String)
    {
        try
        {
            val intent = Intent(Intent.ACTION_CALL)

            intent.data = Uri.parse("tel:$number")

            activity.startActivity(intent)
        }
        catch (e:java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    // function for checking whether the provided email is valid or not
    fun isEmailValid(email: String): Boolean
    {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)

        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    // function for opening date picker dialog
    fun openDatePickerDOB(activity:Activity,from: String, textview: TextView)
    {
        myCalendar = Calendar.getInstance()

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)

            myCalendar.set(Calendar.MONTH, monthOfYear)

            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateLabel(from, textview)  // function for updating the edit text with the provided data
        }

        val datePicker = DatePickerDialog(activity, R.style.CustomPickerTheme, datePickerDialog, myCalendar.get(YEAR), myCalendar.get(MONTH), myCalendar.get(DAY_OF_MONTH))

        datePicker.datePicker.maxDate = myCalendar.timeInMillis /*+ 2592000000*/

        datePicker.setCancelable(false)

        datePicker.show()
    }

    // function for updating the edit text with the provided data
    @SuppressLint("SimpleDateFormat")
    private fun updateLabel(from: String, etDate: TextView?)
    {
        val myFormat = "dd MMM yyyy" //In which you need put here

        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)

        etDate!!.text = sdf.format(myCalendar.time)

        val sdfServer = SimpleDateFormat("yyyy-MM-dd")
        // sdf.timeZone = TimeZone.getTimeZone("UTC")

        if (from == "Start")
            startDateCustom = sdfServer.format(myCalendar.time)

        else
            endDateCustom = sdfServer.format(myCalendar.time)
    }


    // function for getting current date
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(today:String):String
    {
        val c:Date = getInstance().time

        System.out.println("Current time => $c")

        val df  =  SimpleDateFormat("dd MMM")

        df.format(c)

        return   df.format(c).toString()
    }
}
