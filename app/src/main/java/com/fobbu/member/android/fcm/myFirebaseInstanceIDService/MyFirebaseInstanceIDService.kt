package com.fobbu.member.android.fcm.myFirebaseInstanceIDService

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.utils.Url
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
/**
 * Created by abc on 30/1/18.
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService()
{
    lateinit var myPrefs: SharedPreferences

    lateinit var prefsEditor: SharedPreferences.Editor

    lateinit var webServiceApi: WebServiceApi

    @SuppressLint("CommitPrefEdits")
    override fun onTokenRefresh()
    {
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

        if (checkInternetConn(this))
        {
            if (myPrefs.getString("_id", "") != "")
                sendRegistrationToServer(refreshedToken,
                    myPrefs.getString("x_access_token", ""), myPrefs.getString("_id", ""))   // implementing update_device_token API
        }
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

    // ****************************** update_device_token API ************************//

    // implementing update_device_token API
    private fun sendRegistrationToServer(refreshedToken: String, accessToken: String, refreshedId: String)
    {
        try
        {
            webServiceApi = buildRetrofit()               // build retrofit

            val hashmap = HashMap<String, String>()

            hashmap["device_token"] = refreshedToken

            hashmap["user_id"] = refreshedId

            hashmap["device_type"]="android"

            webServiceApi.updateDeviceTokenFCM(hashmap, accessToken).enqueue(object : Callback<MainPojo>
            {
                override fun onFailure(call: Call<MainPojo>, t: Throwable) {}

                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {}
            })
        }catch (e:Exception)
        { }
    }


    // build retrofit
    private fun buildRetrofit(): WebServiceApi
    {
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
            .baseUrl(Url.ApiUrl.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(WebServiceApi::class.java)

        return webServiceApi
    }
}