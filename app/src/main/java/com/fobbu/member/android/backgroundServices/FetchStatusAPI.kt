package com.fobbu.member.android.backgroundServices

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Handler
import android.os.IBinder
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.FetchStatusHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.Url
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FetchStatusAPI : Service() {
    lateinit var mHandler: Handler

    lateinit var rsaLiveHandler: FetchStatusHandler

    val defaultSyncInterval = (30 * 1000).toLong()

    lateinit var myPrefs: SharedPreferences
    lateinit var prefsEditor: SharedPreferences.Editor


    lateinit var retrofit: Retrofit
    lateinit var webServiceApi: WebServiceApi

    private val runnableService = object : Runnable {
        override fun run() {
            syncData()
        }
    }

    private fun syncData() {
        println("HIT API>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

        if(myPrefs.getString(RsaConstants.ServiceSaved.fobbuRequestId, "")!="")
        {
            fetchStatus()

            // Repeat this runnable code block again every ... min
            mHandler.postDelayed(runnableService, defaultSyncInterval)
        }
        else
        {
            println("REQUEST ID EMPTY STOPPING SERVICE>>>>>>>>>>")
            stopSelf()
        }

    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mHandler = Handler()
        // Execute a runnable task as soon as possible
        mHandler.post(runnableService)

        myPrefs = getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        prefsEditor = myPrefs.edit()

        return Service.START_STICKY
    }

    private fun checkInternetConn(con: Context): Boolean {
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

    private fun fetchStatus() {

        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()

        httpClient.networkInterceptors().add(Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header("Content-Type", "application/json")
                .header("API-KEY", "fobbu")
                .method(original.method(), original.body())
                .build()

            chain.proceed(request)
        })
        httpClient.addInterceptor(loggingInterceptor)
        httpClient.readTimeout((2 * 60).toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout((2 * 60).toLong(), TimeUnit.SECONDS)

        val client = httpClient.build()

        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(Url.ApiUrl.URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        webServiceApi = retrofit.create<WebServiceApi>(WebServiceApi::class.java)

        println("TOKEN " + myPrefs.getString("x_access_token", ""))

        val token: String = myPrefs.getString("x_access_token", "")

        val requestId: String = myPrefs.getString(RsaConstants.ServiceSaved.fobbuRequestId, "")

        val pastHistory: Call<MainPojo>? = webServiceApi.getServices(token, requestId)

        pastHistory!!.enqueue(object : Callback<MainPojo> {
            override fun onFailure(call: Call<MainPojo>?, t: Throwable?) {
                t!!.printStackTrace()
            }

            override fun onResponse(call: Call<MainPojo>?, response: Response<MainPojo>?) {

                try {
                    val mainPojo = response!!.body()

                    if (mainPojo!!.success == "true") {

                        if(myPrefs.getString(RsaConstants.RsaTypes.checkStatus,"")!=
                            mainPojo.getData().status)
                        {
                            println("SAVED AND MOVED YEAHH")

                            prefsEditor.putString(RsaConstants.ServiceSaved.serviceNameSelected,mainPojo.getData().static_name).apply()

                            prefsEditor.putString(RsaConstants.ServiceSaved.fobbuRequestId, mainPojo.getData()._id).apply()

                            prefsEditor.putString(RsaConstants.RsaTypes.checkIfOnGoingRsaRequest, "YES").apply()

                            prefsEditor.putString(RsaConstants.RsaTypes.checkStatus, mainPojo.getData().status)

                            val otp = if (mainPojo.getData().otp.contains("\\.".toRegex())) {
                                mainPojo.getData().otp.split("\\.".toRegex())[0]
                            } else
                                mainPojo.getData().otp

                            prefsEditor.putString(RsaConstants.ServiceSaved.otpStart, otp).apply()


                            when {
                                mainPojo.getData().status=="cancelled_by_admin" ->
                                {
                                    emptySharedRequest()

                                    val intent = Intent()
                                    intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                                    intent.putExtra("navigate_to","4")
                                    intent.putExtra("message",mainPojo.getData().reason_of_cancellation)
                                    sendBroadcast(intent)
                                }

                                mainPojo.getData().status=="new" -> {

                                }
                                mainPojo.getData().status==FcmPushTypes.Types.accept -> {
                                    val intent = Intent()
                                    intent.action = FcmPushTypes.Types.acceptRequestBroadCast
                                    intent.putExtra("navigate_to","1")
                                    sendBroadcast(intent)
                                }
                                else -> {
                                    val intent = Intent()
                                    intent.action = FcmPushTypes.Types.fromAPIBroadCast
                                    sendBroadcast(intent)
                                }
                            }
                        }
                        else {
                            println("NOT SAVED BECAUSE SAME ")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

    }

    private fun emptySharedRequest() {
        prefsEditor.putString(RsaConstants.ServiceSaved.isNew,"").apply()
        prefsEditor.remove(RsaConstants.ServiceSaved.fobbuRequestId).apply()
        prefsEditor.putString(RsaConstants.RsaTypes.onGoingRsaScreen, "").apply()
        prefsEditor.putString(RsaConstants.RsaTypes.onGoingRsaScreenType, "").apply()
        prefsEditor.putString(RsaConstants.RsaTypes.checkIfOnGoingRsaRequest,"").apply()
        prefsEditor.putString(RsaConstants.RsaTypes.checkStatus,"").apply()
    }


}