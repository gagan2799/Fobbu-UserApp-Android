package com.fobbu.member.android.activities.loginSignupModule.presenter

interface SignUpActivityHandler
{
    // function for providing the parameters of the signup API to the presenter
     fun sendSignUpData(user_type: String,firstName:String,lastName:String,displayName:String,email:String,password:String, mobile:String,gender:String,token:String)
}