package com.fobbu.member.android.apiInterface

import com.fobbu.member.android.modals.MainPojo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart




/**
 * Created by abc on 29/1/18.
 */
interface WebServiceApi {


    @POST("users/signup")
    fun postSignUp(
        @Body map:HashMap<String,String>
    ): Call<MainPojo>


    @POST("users/login")
    fun login(@Body body: HashMap<String,String> ): Call<MainPojo>



    @POST("users/forgot-password")
    fun forgotPassword(
        @Body mapData: HashMap<String, String>
    ): Call<MainPojo>



    @JvmSuppressWildcards
    @Multipart
    @POST("users/vehicles")
    fun addVehicle(
        @PartMap  partMap:Map<String, RequestBody>,
        @Part files :ArrayList<MultipartBody.Part>,
        @Header("x-access-token") token: String)

            : Call<MainPojo>


    @GET("/partners/services")
    fun fetchServices(
        @Header("x-access-token") token: String
    ): Call<MainPojo>


    @JvmSuppressWildcards
    @Multipart
    @POST("users/requests")
    fun findFobbuRequest(
        @Part("user_id")  user_id: RequestBody,
        @Part("service")  service: RequestBody,
        @Part("latitude")  latitude: RequestBody,
        @Part("longitude")  longitude: RequestBody,
        @Part("vehicle_type")  vehicle_type: RequestBody,
        @Part files :ArrayList<MultipartBody.Part>,
        @Header("x-access-token") token: String): Call<MainPojo>


    @PUT("users/requests")
    fun findFobbuRequestUpdateVehicle(
        @Body body: HashMap<String,String>,
        @Header("x-access-token") token: String): Call<MainPojo>


    @GET("users/vehicles")
    fun fetchUserVehicles(
        @Header("x-access-token") token: String,
        @Body mapData: HashMap<String, String>
    ):Call<MainPojo>


    @GET("users/request/{requestId}")
    fun findFleetOrUser(
        @Header("x-access-token") token: String,
        @Path ("requestId") requestId:String
    ):Call<MainPojo>


    @POST("users/update_device_token")
    fun updateDeviceTokenFCM(@Body mapData:HashMap<String,String>,
                             @Header("x-access-token") token:String):Call<MainPojo>

    @POST("partners/request/change_status")
    fun cancellationRequest(@Body mapData:HashMap<String,String>,
                            @Header("x-access-token") token:String) :Call<MainPojo>

    @GET("users/reason?type=user")
    fun getCancellationReason(@Header("x-access-token") token:String):Call<MainPojo>

}