package com.fobbu.member.android.activities.profile.presenter

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileHandler
{
    fun updateUser(email:RequestBody,number:RequestBody, first:RequestBody,last:RequestBody,gender:RequestBody,file:ArrayList<MultipartBody.Part>,token:String)

    fun updateKyc(map:HashMap<String,Any>,token:String)
}