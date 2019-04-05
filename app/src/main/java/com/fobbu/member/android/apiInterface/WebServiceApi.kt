package com.fobbu.member.android.apiInterface

import com.fobbu.member.android.modals.MainPojo
import com.google.android.gms.maps.model.LatLng
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
interface WebServiceApi
{
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
        @Header("x-access-token") token: String): Call<MainPojo>

    @JvmSuppressWildcards
    @Multipart
    @POST("users/vehicles/update_vehicle")
    fun editVehicle(
        @PartMap body: Map<String,RequestBody>,
        @Part files :ArrayList<MultipartBody.Part>,
        @Header("x-access-token") token: String): Call<MainPojo>

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
        @Part("vehicle_number")vehicle_number: RequestBody,
        @Part files :ArrayList<MultipartBody.Part>,
        @Header("x-access-token") token: String): Call<MainPojo>

    @PUT("users/requests")
    fun findFobbuRequestUpdateVehicle(
        @Body body: HashMap<String,String>,
        @Header("x-access-token") token: String): Call<MainPojo>

    @GET("users/vehicles")
    fun fetchUserVehicles(
        @Header("x-access-token") token: String,
        @Query ("user_id") user_id:String
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

    @GET("users/requests")
    fun getServices(
        @Header("x-access-token") token:String,
        @Query("request_id") requestID:String):Call<MainPojo>

    @POST("users/request/make_payment")
    fun makePayment(@Body mapData:HashMap<String,String>,
                    @Header("x-access-token") token:String) :Call<MainPojo>

    @HTTP(method = "DELETE", path = "/users/vehicles", hasBody = true)
    fun deleteVehicle(
        @Header("x-access-token") token: String,
        @Body partMap:Map<String, String>
    ): Call<MainPojo>

    @POST("users/request/provide_ratings")
    @Multipart
    @JvmSuppressWildcards
    fun postReviews(@Part("request_id") requestID:RequestBody,
                    @Part("ratings") rating:RequestBody,
                    @Part("comments") reviews:RequestBody,
                    @Header("x-access-token") token:String):Call<MainPojo>

    @GET("users/get_requests")
    fun getOrders(@Query("type") type:String,
                  @Query("page") pageCount:String,
                  @Header("x-access-token") token:String):Call<MainPojo>

    @POST("users/emergencycontacts")
    fun postEmergencyContacts(
        @Body data:HashMap<String,String>,
        @Header("x-access-token") token: String
    ): Call<MainPojo>

    @PUT("users/emergencycontacts")
    fun editContacts(@Body map:HashMap<String,Any>,
                     @Header("x-access-token") token: String):Call<MainPojo>

    @GET("users/emergencycontacts")
    fun getContacts(@Header("x-access-token") token: String):Call<MainPojo>

    @HTTP(method = "DELETE", path = "users/emergencycontacts", hasBody = true)
    fun deleteContacts(
        @Header("x-access-token") token: String,
        @Body partMap:Map<String, String>
    ): Call<MainPojo>

    @GET("users/get_helps")
    fun  getHelp(@Header("x-access-token") token: String):Call<MainPojo>

    @POST("users/change_password")
    fun changePassword(@Body map:HashMap<String,Any>
                       ,@Header("x-access-token") token: String):Call<MainPojo>

    @POST("users/update_user")
    @Multipart
    @JvmSuppressWildcards
    fun updateUser(@PartMap map:Map<String,RequestBody>
                   ,@Part files:ArrayList<MultipartBody.Part>
                   ,@Header("x-access-token") token: String):Call<MainPojo>

    @PUT("users/update_kyc")
    fun updateKyc(@Body map:HashMap<String,Any>
                  ,@Header("x-access-token") token: String):Call<MainPojo>

    @POST("users/ods_requests")
    fun makeOdsRequest(@Body map:HashMap<String,Any>
                       ,@Header("x-access-token") token: String):Call<MainPojo>

    @GET("geocode/json")
    fun getAddress(@Query("latlng") latlng:String,
                   @Query("sensor") sensor:String,
                   @Query("key") key:String):Call<MainPojo>
}