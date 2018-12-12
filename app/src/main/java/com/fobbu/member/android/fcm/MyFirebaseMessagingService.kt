package com.fobbu.member.android.fcm

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject


/**
 * Created by abc on 30/1/18.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    internal var type: String = ""
    internal var value = ""
    private val TAG = "Firebase Msg"
    val NOTIF_CHANNEL_ID = "com.fobbu.member.android_channel_id_090909"


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {//Displaying data in log

        if (remoteMessage == null)
            return

        Log.e(TAG, "Notification Body: $remoteMessage")

        Log.e(TAG, "Notification Body: " + remoteMessage.data)

        val dataMap: Map<String, String> = remoteMessage.data

        type = remoteMessage.data["type"].toString()

        try {
            // val json = JSONObject(remoteMessage.notification!!.title)

            if (!isAppIsInBackground(this)) {

                ifAppIsOpen(dataMap)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendNotificationPush(dataMap)
                } else
                    ifAppIsNotOpenSendNotification(dataMap)

            }


        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
        //handleNotification(remoteMessage!!.notification!!.body)

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun sendNotificationPush(map: Map<String, String>) {

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        @SuppressLint("WrongConstant") val nc =
            NotificationChannel(NOTIF_CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_MAX)
        nc.description = ""
        nc.enableLights(true)
        nc.lightColor = Color.GREEN
        nm.createNotificationChannel(nc)

        val intent = Intent(this, DashboardActivity::class.java)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val json = JSONObject(map["notification"])

        if (json["type"] == FcmPushTypes.Types.accept) {
            intent.putExtra("from_push", json["type"].toString())
        } else if (json["type"] == FcmPushTypes.Types.inRouteRequest) {
            intent.putExtra("from_push", json["type"].toString())
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder = NotificationCompat.Builder(
            this, NOTIF_CHANNEL_ID
        )
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

    private fun ifAppIsOpen(map: Map<String, String>) {

        val json = JSONObject(map["notification"])

        if (json["type"] == FcmPushTypes.Types.accept) {
            val intent = Intent()
            intent.action = FcmPushTypes.Types.acceptRequestBroadCast
            intent.putExtra("navigate_to", "1")
            sendBroadcast(intent)
        } else if (json["type"] == FcmPushTypes.Types.inRouteRequest) {
            val intent = Intent()
            intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
            intent.putExtra("navigate_to", FcmPushTypes.Types.inRouteRequest)
            sendBroadcast(intent)
        }
    }

    private fun ifAppIsNotOpenSendNotification(map: Map<String, String>) {

        val intent = Intent(this, DashboardActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val json = JSONObject(map["notification"])

        if (json["type"] == FcmPushTypes.Types.accept) {
            intent.putExtra("from_push", json["type"].toString())
        } else if (json["type"] == FcmPushTypes.Types.inRouteRequest) {
            intent.putExtra("from_push", json["type"].toString())
        }

        startActivity(intent)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder = NotificationCompat.Builder(
            this
        )
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

    private fun getNotificationIcon(): Int {
        val whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

        return if (whiteIcon) R.mipmap.app_icon else R.mipmap.app_icon
    }


    /**
     * Method checks if the app is in background or not
     */
    fun isAppIsInBackground(context: Context): Boolean {
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