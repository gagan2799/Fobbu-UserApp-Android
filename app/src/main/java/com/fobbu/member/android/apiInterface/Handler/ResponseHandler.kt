package com.fobbu.member.android.apiInterface.Handler

import com.fobbu.member.android.modals.MainPojo


interface ResponseHandler
{
    fun onSuccess(mainPojo: MainPojo)
    fun onError(message:String)
    fun onServerError(message:String)
    fun on401()
}