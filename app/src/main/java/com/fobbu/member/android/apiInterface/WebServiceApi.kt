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

    @FormUrlEncoded
    @POST("users/signup")
    fun postSignUp(
        @Field("user_type") user_type: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("display_name") display_name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("mobile_number") mobile_number: String,
        @Field("gender") gender: String,
        @Field("device_token") device_token: String

    ): Call<MainPojo>


    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("mobile_number") mobile_number: String,
        @Field("password") assignee_password: String,
        @Field("device_token") device_token: String

    ): Call<MainPojo>


    @FormUrlEncoded
    @POST("users/forgot-password")
    fun forgotPassword(
        @Field("email") email: String

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
        @Query ("user_id") userId:String
    ):Call<MainPojo>


    @GET("users/request/{requestId}")
    fun findFleetOrUser(
        @Header("x-access-token") token: String,
        @Path ("requestId") requestId:String
    ):Call<MainPojo>


}