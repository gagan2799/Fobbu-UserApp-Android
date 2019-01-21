package com.fobbu.member.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.NotificationManager
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
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginSignupModule.LoginActivity
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaClassType
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.option_menu_layout.*
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
/**
 * Created by abc on 12/1/18.
 */
class CommonClass(activity1: Activity, context1: Context) {
    private var activity: Activity = activity1

    private var context: Context = context1


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
        prefsEditor.putString(RsaClassType.RsaTypes.onGoingRsaScreen, "")
        prefsEditor.apply()
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

            val currentDate = Calendar.getInstance().time
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


}
