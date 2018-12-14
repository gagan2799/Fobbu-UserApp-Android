package com.fobbu.member.android.activities.vehicleModule.presenter

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AddEditActivityHandler {
     fun sendAddEditData(map:Map<String, RequestBody>, list:ArrayList<MultipartBody.Part>, tokenHeader:String)
     fun findFobbuRequestUpdateVehicle(map:HashMap<String,String>,token:String)
     fun deleteVehicle(token:String,vehicleId:String,userId:String)
     fun sendEditData(map:Map<String, RequestBody>, list:ArrayList<MultipartBody.Part>, tokenHeader:String)
}