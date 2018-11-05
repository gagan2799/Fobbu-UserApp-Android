package com.fobbu.member.android.fcm

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by abc on 30/1/18.
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService()
{

    lateinit var myPrefs: SharedPreferences

    lateinit var prefsEditor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onTokenRefresh() {

        //Getting registration token
        val refreshedToken = FirebaseInstanceId.getInstance().token
        val refreshedId = FirebaseInstanceId.getInstance().id

        System.out.println("Refreshed ID: $refreshedId")
        //Displaying token on logcat
        System.out.println("Refreshed token: $refreshedToken")

        myPrefs = this.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        prefsEditor = myPrefs.edit()

        prefsEditor.putString("device_token", refreshedToken).commit()
        prefsEditor.putString("device_ID", refreshedId).commit()

        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?) {

    }
}