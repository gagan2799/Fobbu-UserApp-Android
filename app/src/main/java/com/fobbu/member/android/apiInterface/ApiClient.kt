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
       val map=HashMap<String,String>()
       map["mobile_number"]=mobile
       map["password"]=password
       map["device_token"]=token

       webServiceApi.login(map).enqueue(object :Callback<MainPojo>
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
        val map=HashMap<String,String>()
        map["user_type"]=user_type
        map["first_name"]=firstName
        map["last_name"]=lastName
        map["display_name"]=displayName
        map["email"]=email
        map["password"]=password
        map["mobile_number"]=mobile
        map["gender"]=gender
        map["device_token"]=token

        webServiceApi.postSignUp(map)
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
        val map=HashMap<String,String>()
        map["user_id"]=userId
        webServiceApi.fetchUserVehicles(token,map).enqueue(object :Callback<MainPojo>
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
        val map=HashMap<String,String>()
        map["email"]=email
        webServiceApi.forgotPassword(map).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                handleSuccess(response,responseHandler)
            }

        })
    }

    // request change status api(Cancellation Request)
     fun cancellationRequest (reason:String,requestId:String,token:String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,String>()
        map["reason_of_cancellation"]=reason
        map["status"]="cancelled_by_user"
        map["request_id"]=requestId
        webServiceApi.cancellationRequest(map,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                handleSuccess(response,responseHandler)
            }

        })

    }


    // cancel reasons Api
    fun cancelReasons(token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.getCancellationReason(token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                handleSuccess(response,responseHandler)
            }

        })
    }

    // fetch service Api (Rsa Fragment)
     fun fetchService(token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.fetchServices(token).enqueue(object :Callback<MainPojo>
        {
            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                handleSuccess(response,responseHandler)
            }

            override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                responseHandler.onServerError(""+t.message)
            }

        })
    }

    // find fobbu Request(RSA Fragment)
     fun findFobbuRequest(userId:RequestBody,serviceSelected:RequestBody,strtLatitude:RequestBody,strLongitude:RequestBody
    ,strVehicleType:RequestBody,fileList:ArrayList<MultipartBody.Part>,token:String,responseHandler: ResponseHandler)
    {
        /*val  map= HashMap<String,RequestBody>()
        map["user_id"]=userId
        map["service"]=serviceSelected
        map["latitude"]=strtLatitude
        map["longitude"]=strLongitude
        map["vehicle_type"]=strVehicleType*/
        webServiceMultipart.findFobbuRequest(userId,serviceSelected,strtLatitude,strLongitude,strVehicleType,fileList,token)
            .enqueue(object :Callback<MainPojo>
            {
                override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                    responseHandler.onServerError(""+t.message)
                }

                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {
                    handleSuccess(response,responseHandler)
                }

            })
    }

    // fleet request api(RSA Fragment)
    fun findFleetOrUser (token:String,requestId:String,responseHandler: ResponseHandler)
    {
        webServiceApi.findFleetOrUser(token,requestId).enqueue(object :Callback<MainPojo>
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
            println("main pojo data $response")
        }
    }

    private fun getEnv(): MyApplication {
        return activity.application as MyApplication
    }

}