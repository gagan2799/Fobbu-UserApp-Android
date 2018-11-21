package com.fobbu.member.android.activities.loginSignupModule.presenter

interface LoginActivityHandler {
    fun sendLoginData(mobile:String,password:String,token :String)
}