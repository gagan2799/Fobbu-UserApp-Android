package com.fobbu.member.android.apiInterface

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart


class ApiClient(var activity: Activity) {

     private var webServiceApi:WebServiceApi = getEnv().getRetrofit()

     private  var webServiceMultipart:WebServiceApi=getEnv().getRetrofitMulti()



   //login api
    fun getLoginData(mobile:String, password:String, token:String, responseHandler: ResponseHandler)
   {
       webServiceApi.login(mobile,password,token).enqueue(object :Callback<MainPojo>
       {
           override fun onFailure(call: Call<MainPojo>, t: Throwable) {
               responseHandler.onServerError(""+t.message)
           }

           override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
               handleSuccess(response,responseHandler)
           }

       })
   }



    // signup api
     fun getSignupData(user_type: String,firstName:String,lastName:String,displayName:String,email:String,password:String,
    mobile:String,gender:String,token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.postSignUp(user_type,firstName,lastName,displayName,email,password,mobile,gender,token)
            .enqueue(object:Callback<MainPojo>
            {
                override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                    responseHandler.onServerError(""+t.message)
                }

                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                  handleSuccess(response,responseHandler)
                }

            })
    }


    //add_edit_vechile_api
    fun getAddEditVehicleData(map:Map<String,RequestBody>,list:ArrayList<MultipartBody.Part>,tokenHeader:String,responseHandler: ResponseHandler)
    {
        webServiceMultipart.addVehicle(map,list,tokenHeader).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                handleSuccess(response,responseHandler)
            }

        })
    }


    //vehicle_list_api
    fun getVichleListData(token:String,userId:String,responseHandler: ResponseHandler)
    {
        webServiceApi.fetchUserVehicles(token,userId).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                handleSuccess(response,responseHandler)
            }

        })
    }

    // vehicle list update api
    fun findFobbuRequestUpdateVehicle(haspmap:HashMap<String,String>,token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.findFobbuRequestUpdateVehicle(haspmap,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
               handleSuccess(response,responseHandler)
            }

        }
        )
    }

    // forgot password api
     fun forgotPassword(email:String,responseHandler: ResponseHandler)
    {
        webServiceApi.forgotPassword(email).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                handleSuccess(response,responseHandler)
            }

        })
    }


    // method for handling the response
    fun handleSuccess(response:Response<MainPojo>,responseHandler: ResponseHandler)
    {
        if (response.isSuccessful)
        {
            if (response.body()!= null)
            {
                responseHandler.onSuccess(response.body()!!)
            }
        }else{
            responseHandler.onError(""+response.message())
        }
    }

    private fun getEnv(): MyApplication {
        return activity.application as MyApplication
    }

}