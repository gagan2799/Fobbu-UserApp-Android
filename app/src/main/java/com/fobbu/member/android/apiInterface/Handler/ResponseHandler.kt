package com.fobbu.member.android.apiInterface.Handler

import com.fobbu.member.android.modals.MainPojo


interface ResponseHandler
{
    // function for providing response from the API
    fun onSuccess(mainPojo: MainPojo)

    // function for showing error message from API
    fun onError(message:String)

    // function for showing message in case of failure
    fun onServerError(message:String)

    // function for handling the case of response code 401
    fun on401()
}