package com.fobbu.member.android.activities.rsaModule.presenter

interface RsaCancelRequestHandler {
     fun cancelRequest(reason:String,requestId:String,token:String)
     fun cancelReasons(token:String)
}