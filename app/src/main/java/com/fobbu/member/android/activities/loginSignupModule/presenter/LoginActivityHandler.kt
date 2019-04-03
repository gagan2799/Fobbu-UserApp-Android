package com.fobbu.member.android.activities.loginSignupModule.presenter

interface LoginActivityHandler
{
    fun sendLoginData(mobile:String,password:String,token :String)    // function for providing the parameter  of login API to the presenter
}