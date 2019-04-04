package com.fobbu.member.android.activities.rsaModule.presenter

interface RsaCancelRequestHandler
{
     // function for providing the parameters of the change_status API to the presenter
     fun cancelRequest(reason:String,requestId:String,token:String)

     // function for providing the parameters of the reason?type=user API to the presenter
     fun cancelReasons(token:String)
}