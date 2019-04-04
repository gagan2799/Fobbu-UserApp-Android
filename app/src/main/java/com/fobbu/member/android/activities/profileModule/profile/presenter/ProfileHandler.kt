package com.fobbu.member.android.activities.profile.presenter

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileHandler
{
    // function for providing the parameter of the update_user API to the presenter
    fun updateUser(email:RequestBody,number:RequestBody, first:RequestBody,last:RequestBody,gender:RequestBody,file:ArrayList<MultipartBody.Part>,token:String)

    // function for providing the parameters of the update_kyc API to the presenter
    fun updateKyc(map:HashMap<String,Any>,token:String)
}