package com.fobbu.member.android.activities.vehicleModule.presenter

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AddEditActivityHandler
{
     // function providing the parameters of the users/vehicles (POST) API to the presenter
     fun sendAddEditData(map:Map<String, RequestBody>, list:ArrayList<MultipartBody.Part>, tokenHeader:String)

     // function providing the parameters of the users/requests API to the presenter
     fun findFobbuRequestUpdateVehicle(map:HashMap<String,String>,token:String)

     // function providing the parameters of the users/vehicles (DELETE) API to the presenter
     fun deleteVehicle(token:String,vehicleId:String,userId:String)

     // function providing the parameters of the update_vehicle API to the presenter
     fun sendEditData(map:Map<String, RequestBody>, list:ArrayList<MultipartBody.Part>, tokenHeader:String)
}