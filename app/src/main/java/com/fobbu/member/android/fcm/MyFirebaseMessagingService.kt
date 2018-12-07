package com.fobbu.member.android.fcm

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenBlue
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


/**
 * Created by abc on 30/1/18.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    internal var type: String = ""
    internal var value = ""
    internal var message = ""
    private val TAG = "Firebase Msg"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {//Displaying data in log

        if (remoteMessage == null)
            return

        Log.e(TAG, "Notification Body: $remoteMessage")

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.data["type"])

            type = remoteMessage.data["type"].toString()

            try {
               // val json = JSONObject(remoteMessage.notification!!.title)

                if(!isAppIsInBackground(this)){

                    ifAppIsOpen(type)
                }
                else
                {
                    ifAppIsNotOpenSendNotification(type,remoteMessage)
                }


            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
            //handleNotification(remoteMessage!!.notification!!.body)
        }
    }

    private fun ifAppIsOpen(type: String) {

        if(type ==FcmPushTypes.Types.accept)
        {
            val intent =Intent()
            intent.action = FcmPushTypes.Types.acceptRequestBroadCast
            intent.putExtra("navigate_to","1")
            sendBroadcast(intent)
        }
        else if(type ==FcmPushTypes.Types.inRouteRequest)
        {
            val intent =Intent()
            intent.action = FcmPushTypes.Types.inRouteRequestBroadCast
            intent.putExtra("navigate_to",FcmPushTypes.Types.inRouteRequest)
            sendBroadcast(intent)
        }
    }

    private fun ifAppIsNotOpenSendNotification(type: String, remoteMessage: RemoteMessage) {

        val intent = Intent(this, DashboardActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        if(type ==FcmPushTypes.Types.accept)
        {
            intent.putExtra("from_push",type)
        }
        else if(type ==FcmPushTypes.Types.inRouteRequest)
        {
            intent.putExtra("from_push",type)
        }

        startActivity(intent)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(
                this)
                .setSmallIcon(getNotificationIcon())

                .setContentTitle(resources.getString(R.string.app_name)).setContentText(message)

                .setStyle(NotificationCompat.BigTextStyle().bigText(message))

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