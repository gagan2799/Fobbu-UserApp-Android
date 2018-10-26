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
        @Field("gender") gender: String

    ): Call<MainPojo>


    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("mobile_number") mobile_number: String,
        @Field("password") assignee_password: String

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
        @Part files :ArrayList<MultipartBody.Part> )

            : Call<MainPojo>

}