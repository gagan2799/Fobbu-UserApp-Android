package com.fobbu.member.android.activities.changePassword.presenter

interface PasswordHandler
{
    fun changePassword(password:String,token:String)   // function for providing the parameters of change password API to the presenter
}