package com.fobbu.member.android.fcm

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.DashboardActivity
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import retrofit2.Retrofit


/**
 * Created by abc on 30/1/18.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyFirebaseMessagingService : FirebaseMessagingService()
{
    internal var type: String = ""

    internal var value = ""

    private val TAG = "Firebase Msg"

    private val NOTIF_CHANNEL_ID = "com.fobbu.member.android_channel_id_090909"

    lateinit var myPrefs: SharedPreferences

    lateinit var prefsEditor: SharedPreferences.Editor

    lateinit var retrofit: Retrofit

    lateinit var webServiceApi: WebServiceApi

    override fun onMessageReceived(remoteMessage: RemoteMessage?)
    {
        //Displaying data in log
        if (remoteMessage == null)
            return

        myPrefs = this.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        prefsEditor = myPrefs.edit()

        Log.e(TAG, "Notification Body: $remoteMessage")

        Log.e(TAG, "Notification Body: " + remoteMessage.data)

        try
        {
            val dataMap: Map<String, String> = remoteMessage.data

            val type = remoteMessage.data["type"].toString()

            prefsEditor.putString(RsaConstants.ServiceSaved.cancellationMessage, dataMap["body"]).apply()

            val json = JSONObject(dataMap["notification"])

            if (json["type"].toString()== FcmPushTypes.Types.cancelledByAdmin)
            {
                prefsEditor.putString(RsaConstants.RsaTypes.checkStatus, dataMap["type"]).apply()

                prefsEditor.putString(RsaConstants.ServiceSaved.isNew,"").apply()
            }
            println("MESSAGE MAP >>>>>>>> " + isAppIsInBackground(this))       // Method checks if the app is in background or not

            if (!isAppIsInBackground(this))   // Method checks if the app is in background or not
                ifAppIsOpen(dataMap)        // function for changing screens if the app is open

            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    sendNotificationPushO(dataMap)         // function for sending notification to OS OREO or above

                else
                    ifAppIsNotOpenSendNotification(dataMap)         // function for sending notification to OS below OREO
            }
        }
        catch (e: Exception)
        {
            Log.e("", "Exception: " + e.message)
        }
    }

    // function for sending notification to OS OREO or above
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun sendNotificationPushO(map: Map<String, String>)
    {
        val intent1 = Intent()

        intent1.action = FcmPushTypes.Types.checkStatusPushOneTime

        sendBroadcast(intent1)

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        @SuppressLint("WrongConstant") val nc = NotificationChannel(NOTIF_CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_MAX)

        nc.description = ""

        nc.enableLights(true)

        nc.lightColor = Color.GREEN

        nm.createNotificationChannel(nc)

        val intent = Intent(this, DashboardActivity::class.java)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val json = JSONObject(map["notification"])

       // prefsEditor.putString(RsaConstants.RsaTypes.checkStatus, json["type"].toString()).apply()

        when
        {
            json["type"] == FcmPushTypes.Types.accept ->
            {
                intent.putExtra("from_push", json["type"].toString())

                prefsEditor.putString(RsaConstants.ServiceSaved.isNew,"").apply()
            }

            json["type"] == FcmPushTypes.Types.inRouteRequest ->
                intent.putExtra("from_push", json["type"].toString())

            json["type"] == FcmPushTypes.Types.newPin ->
            {
                intent.putExtra("from_push", FcmPushTypes.Types.newPin)

                intent.putExtra("otp", json["otp"].toString())

                prefsEditor.putString(RsaConstants.ServiceSaved.otpStart, json["otp"].toString()).apply()
            }

            json["type"] == FcmPushTypes.Types.otpVerified ->
                intent.putExtra("from_push", FcmPushTypes.Types.otpVerified)

            json["type"] == FcmPushTypes.Types.moneyRequested ->
                intent.putExtra("from_push", FcmPushTypes.Types.moneyRequested)

            json["type"] == FcmPushTypes.Types.startedWork ->
                intent.putExtra("from_push", FcmPushTypes.Types.startedWork)

            json["type"] == FcmPushTypes.Types.workEnded ->
                intent.putExtra("from_push", FcmPushTypes.Types.workEnded)

            json["type"] == FcmPushTypes.Types.cancelledByAdmin ->
            {
                intent.putExtra("from_push", FcmPushTypes.Types.cancelledByAdmin)

                intent.putExtra("message", map["body"])
            }
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)

            .setSmallIcon(getNotificationIcon())

            .setContentTitle(map["body"]).setContentText(map["body"])

            .setStyle(NotificationCompat.BigTextStyle().bigText(map["body"]))

            .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission

            .setPriority(NotificationCompat.PRIORITY_HIGH)

            .setAutoCancel(true)

            .setSound(defaultSoundUri)

            .setChannelId(NOTIF_CHANNEL_ID)

            .setContentIntent(pendingIntent)

        nm.notify(0, notificationBuilder.build())
    }

    // function for changing screens if the app is open
    private fun ifAppIsOpen(map: Map<String, String>)
    {
        val json = JSONObject(map["notification"])

        println("MESSAGE MAP IF OPEN >>>>>>>> " + json["type"].toString())

        val intent = Intent()

        intent.action = FcmPushTypes.Types.checkStatusPushOneTime

        intent.putExtra("ForCancelScreen",json["type"].toString())

        sendBroadcast(intent)

        //prefsEditor.putString(RsaConstants.RsaTypes.checkStatus, json["type"].toString()).apply()

/*
        when {
            json["type"] == FcmPushTypes.Types.accept -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                intent.putExtra("navigate_to", "1")
                sendBroadcast(intent)
            }

            json["type"] == FcmPushTypes.Types.cancelledByAdmin -> {

                val intent = Intent()
                intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                intent.putExtra("navigate_to", "4")
                intent.putExtra("message", map["body"])
                sendBroadcast(intent)
            }
            json["type"] == FcmPushTypes.Types.inRouteRequest -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
                intent.putExtra("navigate_to", FcmPushTypes.Types.inRouteRequest)
                sendBroadcast(intent)
            }
            json["type"] == FcmPushTypes.Types.newPin -> {

                prefsEditor.putString(RsaConstants.ServiceSaved.otpStart, json["otp"].toString()).apply()

                val intent = Intent()
                intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
                intent.putExtra("navigate_to", FcmPushTypes.Types.newPin)
                intent.putExtra("otp", json["otp"].toString())
                sendBroadcast(intent)
            }
            json["type"] == FcmPushTypes.Types.otpVerified -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
                intent.putExtra("navigate_to", FcmPushTypes.Types.otpVerified)
                sendBroadcast(intent)
            }

            json["type"] == FcmPushTypes.Types.moneyRequested -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.moneyRequestedBroadcast
                intent.putExtra("navigate_to", FcmPushTypes.Types.moneyRequested)
                sendBroadcast(intent)
            }
            json["type"] == FcmPushTypes.Types.startedWork -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.startedWorkBroadcast
                intent.putExtra("navigate_to", FcmPushTypes.Types.startedWork)
                sendBroadcast(intent)
            }
            json["type"] == FcmPushTypes.Types.workEnded -> {
                val intent = Intent()
                intent.action = FcmPushTypes.Types.startedWorkBroadcast
                intent.putExtra("navigate_to", FcmPushTypes.Types.workEnded)
                sendBroadcast(intent)
            }
        }
*/
    }

    // function for sending notification to OS below OREO
    private fun ifAppIsNotOpenSendNotification(map: Map<String, String>)
    {
        val intent1 = Intent()

        intent1.action = FcmPushTypes.Types.checkStatusPushOneTime

        sendBroadcast(intent1)

        val intent = Intent(this, DashboardActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val json = JSONObject(map["notification"])

       // prefsEditor.putString(RsaConstants.RsaTypes.checkStatus, json["type"].toString()).apply()

        // must requires VIBRATE permission
        when
        {
            json["type"] == FcmPushTypes.Types.accept -> intent.putExtra("from_push", json["type"].toString())

            json["type"] == FcmPushTypes.Types.inRouteRequest -> intent.putExtra("from_push", json["type"].toString())

            json["type"] == FcmPushTypes.Types.newPin ->
            {
                intent.putExtra("from_push", FcmPushTypes.Types.newPin)

                intent.putExtra("otp", json["otp"].toString())

                prefsEditor.putString(RsaConstants.ServiceSaved.otpStart, json["otp"].toString()).apply()
            }

            json["type"] == FcmPushTypes.Types.otpVerified ->
                intent.putExtra("from_push", FcmPushTypes.Types.otpVerified)

            json["type"] == FcmPushTypes.Types.moneyRequested ->
                intent.putExtra("from_push", FcmPushTypes.Types.moneyRequested)

            json["type"] == FcmPushTypes.Types.startedWork ->
                intent.putExtra("from_push", FcmPushTypes.Types.startedWork)

            json["type"] == FcmPushTypes.Types.workEnded ->
                intent.putExtra("from_push", FcmPushTypes.Types.workEnded)

            json["type"] == FcmPushTypes.Types.cancelledByAdmin ->
            {
                intent.putExtra("from_push", FcmPushTypes.Types.cancelledByAdmin)

                intent.putExtra("message", map["body"])
            }

            json["type"] == FcmPushTypes.Types.requestCancelled ->
                intent.putExtra("from_push", FcmPushTypes.Types.requestCancelled)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)

            .setSmallIcon(getNotificationIcon())

            .setContentTitle(resources.getString(R.string.app_name)).setContentText(map["body"])

            .setStyle(NotificationCompat.BigTextStyle().bigText(map["body"]))

            .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission

            .setPriority(NotificationCompat.PRIORITY_HIGH)

            .setAutoCancel(true)

            .setSound(defaultSoundUri)

            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getNotificationIcon(): Int
    {
        val whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

        return if (whiteIcon) R.mipmap.app_icon else R.mipmap.app_icon
    }


    /**
     * Method checks if the app is in background or not
     */
    private fun isAppIsInBackground(context: Context): Boolean
    {
        var isInBackground = true

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH)
        {
            val runningProcesses = am.runningAppProcesses

            for (processInfo in runningProcesses)
            {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                {
                    println("LIST >>>>>> $processInfo")

                    println("LIST >>>>>> $processInfo")

                    for (activeProcess in processInfo.pkgList)
                    {
                        if (activeProcess == context.packageName)
                            isInBackground = false
                    }
                }
                else if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
                    println("TERMINATED >>>>>>>>>>")
            }
        }
        else
        {
            println("HERE IN THIS ONE >>>>>> ")

            val taskInfo = am.getRunningTasks(1)

            val componentInfo = taskInfo[0].topActivity

            if (componentInfo.packageName == context.packageName)
                isInBackground = false
        }
        return isInBackground
    }
}