package com.fobbu.member.android.apiInterface

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDex
import com.fobbu.member.android.utils.Url
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by abc on 29/1/18.
 */
class MyApplication : Application() {

    lateinit var myPrefs: SharedPreferences

    lateinit var retrofit: Retrofit

    lateinit var webServiceApi: WebServiceApi

    lateinit var webServiceApiMultipart: WebServiceApi

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        myPrefs = this.getSharedPreferences("Fobbu_Member_Prefs", Context.MODE_PRIVATE)

        forSimpleAPI()

        forMultiPartAPI()
    }

    private fun forMultiPartAPI() {
        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.networkInterceptors().add(Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
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

        webServiceApiMultipart = retrofit.create<WebServiceApi>(WebServiceApi::class.java!!)
    }

    private fun forSimpleAPI() {
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

        webServiceApi = retrofit.create<WebServiceApi>(WebServiceApi::class.java!!)
    }

    fun getRetrofit(): WebServiceApi {
        return webServiceApi
    }

    fun getRetrofitMulti(): WebServiceApi {
        return webServiceApiMultipart
    }

}