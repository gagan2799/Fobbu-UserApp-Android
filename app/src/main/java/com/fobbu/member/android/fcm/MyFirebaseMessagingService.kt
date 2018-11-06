package com.fobbu.member.android.fcm

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
import com.fobbu.member.android.activities.DashboardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject


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
            Log.e(TAG, "Notification Body: " + remoteMessage.notification!!.title)

            try {
                val json = JSONObject(remoteMessage.notification!!.title)
                sendNotification(remoteMessage.notification!!.body.toString(),json.getString("notification_type"))
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
            //handleNotification(remoteMessage!!.notification!!.body)
        }

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())

            try {
                val json = JSONObject(remoteMessage.data.toString())
                //handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }

        }
    }

    private fun sendNotification(message: String, type: String) {

        val intent: Intent? = Intent(this, DashboardActivity::class.java)
        intent!!.putExtra("from_fcm_notification", type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

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
}