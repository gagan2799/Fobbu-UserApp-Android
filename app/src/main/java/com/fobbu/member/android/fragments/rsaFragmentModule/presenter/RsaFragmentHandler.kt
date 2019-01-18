package com.fobbu.member.android.fragments.rsaFragmentModule.presenter

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RsaFragmentHandler {
    fun fetchService(token:String)
    fun findFobbuRequest(userId: RequestBody, serviceSelected: RequestBody, strtLatitude: RequestBody, strLongitude: RequestBody
                         , strVehicleType: RequestBody,strVehicleNumber: RequestBody, fileList:ArrayList<MultipartBody.Part>, token:String)
    fun findFleetOrUser(token:String,requestId:String)
}