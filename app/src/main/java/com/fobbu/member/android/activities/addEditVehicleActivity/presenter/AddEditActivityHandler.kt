package com.fobbu.member.android.activities.addEditVehicleActivity.presenter

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AddEditActivityHandler {
     fun sendAddEditData(map:Map<String, RequestBody>, list:ArrayList<MultipartBody.Part>, tokenHeader:String)
}