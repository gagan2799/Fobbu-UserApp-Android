package com.fobbu.member.android.fcm.myFirebaseInstanceIDService

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

/**
 * Created by abc on 30/1/18.
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    lateinit var myPrefs: SharedPreferences

    lateinit var prefsEditor: SharedPreferences.Editor
    private val BASE_URL: String = "http://104.211.100.239:3000/"
    lateinit var webServiceApi: WebServiceApi


    @SuppressLint("CommitPrefEdits")
    override fun onTokenRefresh() {

        //Getting registration token
        val refreshedToken: String = FirebaseInstanceId.getInstance().token!!
        val refreshedId = FirebaseInstanceId.getInstance().id

        System.out.println("Refreshed ID: $refreshedId")
        //Displaying token on logcat
        System.out.println("Refreshed token: $refreshedToken")

        myPrefs = this.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        prefsEditor = myPrefs.edit()

        prefsEditor.putString("device_token", refreshedToken).commit()
        prefsEditor.putString("device_ID", refreshedId).commit()

        if (myPrefs.getString("_id", "") != "")
            sendRegistrationToServer(refreshedToken,
                myPrefs.getString("x_access_token", ""), myPrefs.getString("_id", ""))
    }

    private fun sendRegistrationToServer(refreshedToken: String, accessToken: String, refreshedId: String) {

        try {
            webServiceApi = buildRetrofit()
            val hashmap = HashMap<String, String>()
            hashmap["device_token"] = refreshedToken
            hashmap["user_id"] = refreshedId

            webServiceApi.updateDeviceTokenFCM(hashmap, accessToken).enqueue(object : Callback<MainPojo> {
                override fun onFailure(call: Call<MainPojo>, t: Throwable) {

                }

                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {

                }

            })
        }catch (e:Exception)
        {
        }

    }


    // build retrofit
    private fun buildRetrofit(): WebServiceApi {

        val httpClient = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor { chain ->
            val original = chain.request()

            val request: Request = original.newBuilder()
                .header("API-KEY", "fobbu")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .method(original.method(), original.body())
                .build()

            chain.proceed(request)
        }

        httpClient.addInterceptor(loggingInterceptor)
        val okHttpClient: OkHttpClient = httpClient.build()

        webServiceApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(WebServiceApi::class.java)
        return webServiceApi
    }


}