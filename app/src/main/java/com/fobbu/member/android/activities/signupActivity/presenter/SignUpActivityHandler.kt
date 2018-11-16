package com.fobbu.member.android.activities.signupActivity.presenter

interface SignUpActivityHandler {
     fun sendSignUpData(user_type: String,firstName:String,lastName:String,displayName:String,email:String,password:String,
                        mobile:String,gender:String,token:String)
}