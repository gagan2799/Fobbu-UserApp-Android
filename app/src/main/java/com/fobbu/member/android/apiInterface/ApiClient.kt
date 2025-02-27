package com.fobbu.member.android.apiInterface

import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.RequestHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ApiClient(var activity: Activity)
{
     private var webServiceApi:WebServiceApi = getEnv().getRetrofit()

     private  var webServiceMultipart:WebServiceApi=getEnv().getRetrofitMulti()

    private  var mapWebService:WebServiceApi=getEnv().getMapService()

    //geocoding
    fun getAddress(latlng:LatLng,sensor:String,key:String,responseHandler: ResponseHandler)
    {
        mapWebService.getAddress(""+latlng.latitude+","+latlng.longitude,sensor,key).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)           // method for handling the response
            }
        })
    }

   //login api
    fun getLoginData(mobile:String, password:String, token:String, responseHandler: ResponseHandler)
   {
       val map=HashMap<String,String>()

       map["mobile_number"]=mobile

       map["password"]=password

       map["device_token"]=token

       map["device_type"]="android"

       webServiceApi.login(map).enqueue(object :Callback<MainPojo>
       {
           override fun onFailure(call: Call<MainPojo>, t: Throwable)
           {
               responseHandler.onServerError(""+t.message)
           }

           override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
           {
               handleSuccess(response,responseHandler)        // method for handling the response
           }
       })
   }

    // signUp api
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

        map["device_type"]="android"

        webServiceApi.postSignUp(map).enqueue(object:Callback<MainPojo>
            {
                override fun onFailure(call: Call<MainPojo>, t: Throwable)
                {
                    responseHandler.onServerError(""+t.message)
                }

                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
                {
                  handleSuccess(response,responseHandler)       // method for handling the response
                }
            })
    }

    //add_edit_vehicle_api
    fun getAddEditVehicleData(map:Map<String,RequestBody>,list:ArrayList<MultipartBody.Part>,tokenHeader:String,responseHandler: ResponseHandler)
    {
        webServiceMultipart.addVehicle(map,list,tokenHeader).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)       // method for handling the response
            }
        })
    }

    // edit_vehicle_api
    fun getEditVehicleData(map:Map<String,RequestBody>,list:ArrayList<MultipartBody.Part>,tokenHeader:String,responseHandler: ResponseHandler)
    {
        webServiceMultipart.editVehicle(map,list,tokenHeader).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)       // method for handling the response
            }
        })
    }

    //vehicle_list_api
    fun getVichleListData(token:String,userId:String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,String>()

        map["user_id"]=userId

        webServiceApi.fetchUserVehicles(token,userId).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)         // method for handling the response
            }
        })
    }

    // vehicle list update api
    fun findFobbuRequestUpdateVehicle(haspmap:HashMap<String,String>,token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.findFobbuRequestUpdateVehicle(haspmap,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
               handleSuccess(response,responseHandler)       // method for handling the response
            }
        })
    }

    // forgot password api
     fun forgotPassword(email:String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,String>()

        map["email"]=email

        webServiceApi.forgotPassword(map).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)        // method for handling the response
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
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)        // method for handling the response
            }
        })

    }

    // cancel reasons Api
    fun cancelReasons(token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.getCancellationReason(token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)            // method for handling the response
            }
        })
    }

    // fetch service Api (Rsa Fragment)
     fun fetchService(token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.fetchServices(token).enqueue(object :Callback<MainPojo>
        {
            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }

            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }
        })
    }

    // find fobbu Request(RSA Fragment)
     fun findFobbuRequest(userId:RequestBody,serviceSelected:RequestBody,strtLatitude:RequestBody,strLongitude:RequestBody
    ,strVehicleType:RequestBody,strVehicleNumber:RequestBody,fileList:ArrayList<MultipartBody.Part>,token:String,responseHandler: ResponseHandler)
    {
        webServiceMultipart.findFobbuRequest(userId,serviceSelected,strtLatitude,strLongitude,strVehicleType,strVehicleNumber,fileList,token).enqueue(object :Callback<MainPojo>
            {
                override fun onFailure(call: Call<MainPojo>, t: Throwable)
                {
                    responseHandler.onServerError(""+t.message)
                }

                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
                {
                    handleSuccess(response,responseHandler)   // method for handling the response
                }
            })
    }

    // fleet request api(RSA Fragment)
    fun findFleetOrUser (token:String,requestId:String,responseHandler: ResponseHandler)
    {
        webServiceApi.findFleetOrUser(token,requestId).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError(""+t.message)
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)    // method for handling the response
            }
        })
    }

    // request API
    fun getService(token:String,requestId: String, responseHandler: ResponseHandler)
    {
        webServiceApi.getServices(token,requestId).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }

        })
    }


    //make payment API

    fun makePayment(token:String,requestId: String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,String>()

        map["request_id"]=requestId

        webServiceApi.makePayment(map,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)       // method for handling the response
            }
        })
    }

    // update device token API
    fun logout(userID:String,token:String,responseHandler: ResponseHandler)
    {
        val hashmap = HashMap<String, String>()
        hashmap["device_token"] = " "
        hashmap["user_id"] = userID
        hashmap["device_type"]="android"
        webServiceApi.updateDeviceTokenFCM(hashmap,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)        // method for handling the response
            }
        })
    }

    // delete vehicle API
    fun deleteVehicle(token:String,vehicleID:String,userID:String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,String>()

        map["vehicle_id"]=vehicleID

        map["user_id"]=userID

        webServiceApi.deleteVehicle(token,map).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }


    // provide rating api
    fun postReviews(requestID:RequestBody,rating:RequestBody,reviews:RequestBody,token:String,responseHandler: ResponseHandler)
    {
        webServiceMultipart.postReviews(requestID,rating,reviews,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)  // method for handling the response
            }
        })
    }


    // get_requests API
    fun getOrder(type:String,page:String,token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.getOrders(type,page,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
            handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
// POST emergencycontacts API
    fun postEmergencyContacts (contactList:ArrayList<HashMap<String,String>>,token:String,responseHandler: ResponseHandler)
    {
        val jsonArray=JSONArray()

        for (i in contactList.indices)
        {
            val jsonObj=JSONObject(contactList[i])

            jsonArray.put(jsonObj)
        }
        val haspmap = HashMap<String,String>()

        haspmap["contacts"]=jsonArray.toString()

        webServiceApi.postEmergencyContacts(haspmap, token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }


    //  get Help API
    fun getHelp(token:String, responseHandler: ResponseHandler)
    {
        webServiceApi.getHelp(token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }


    //change password API
    fun changePassword(password:String,token: String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,Any>()

        map["new_password"]=password

        webServiceApi.changePassword(map,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }


    // edit contacts API
    fun editContacts(dataList:HashMap<String,Any>,token:String,id:String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,Any>()

        val obj =JSONObject(dataList)

        map["contact_id"]=id

        map["contact"]=obj.toString()

        webServiceApi.editContacts(map,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }


    //getContacts API
    fun getContact(token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.getContacts(token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }

    // delete Contacts
    fun deleteContact(contactId:String, token:String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,String>()

        map["contact_id"]=contactId

        webServiceApi.deleteContacts(token,map).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }

    // method for handling the response
    fun handleSuccess(response:Response<MainPojo>,responseHandler: ResponseHandler)
    {
        if (response.code()== 200)
        {
            if (response.isSuccessful)
            {
                if (response.body()!= null)
                {
                    responseHandler.onSuccess(response.body()!!)
                }
            }
        }

        else if (response.code() == 401)
            responseHandler.on401()

        else
            responseHandler.onError("Error: "+response.message())
    }


    // update user API
    fun updateUser(email:RequestBody,number:RequestBody, first:RequestBody,last:RequestBody,gender:RequestBody,file:ArrayList<MultipartBody.Part>,token:String,responseHandler: ResponseHandler)
    {
        val map=HashMap<String,RequestBody>()

        map["email"]=email

        map["mobile_number"]=number

        map["first_name"]=first

        map["last_name"]=last

        map["gender"]=gender

        webServiceMultipart.updateUser(map,file,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }

    //update KYC API
    fun updateKyc(map:HashMap<String,Any>,token:String,responseHandler: ResponseHandler)
    {
        val hashmap=HashMap<String,Any>()

        val obj=JSONObject(map)

        hashmap["kyc"]=obj.toString()

        println("kyc::::$hashmap")

        webServiceApi.updateKyc(hashmap,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)   // method for handling the response
            }
        })
    }

    // ods_request
    fun makeOdsRequest(map:HashMap<String,Any>,token:String,responseHandler: ResponseHandler)
    {
        webServiceApi.makeOdsRequest(map,token).enqueue(object :Callback<MainPojo>
        {
            override fun onFailure(call: Call<MainPojo>, t: Throwable)
            {
                responseHandler.onServerError("""Server Error: ${t.message}""")
            }

            override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>)
            {
                handleSuccess(response,responseHandler)    // method for handling the response
            }
        })
    }

    private fun getEnv(): MyApplication
    {
        return activity.application as MyApplication
    }

}