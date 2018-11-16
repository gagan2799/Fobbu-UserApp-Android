package com.fobbu.member.android.activities.loginActivity.presenter

interface LoginActivityHandler {
    fun sendLoginData(mobile:String,password:String,token :String)
}