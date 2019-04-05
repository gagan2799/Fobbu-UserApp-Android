package com.fobbu.member.android.fragments.rsaFragmentModule.presenter

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RsaFragmentHandler
{
    // function providing the parameters of the services API to the presenter
    fun fetchService(token:String)

    //   // function providing the parameters of the requests API to the presenter
    fun findFobbuRequest(userId: RequestBody, serviceSelected: RequestBody, strtLatitude: RequestBody, strLongitude: RequestBody
                         , strVehicleType: RequestBody,strVehicleNumber: RequestBody, fileList:ArrayList<MultipartBody.Part>, token:String)

    // function providing the parameters of the request/{requestId} API to the presenter
    fun findFleetOrUser(token:String,requestId:String)
}