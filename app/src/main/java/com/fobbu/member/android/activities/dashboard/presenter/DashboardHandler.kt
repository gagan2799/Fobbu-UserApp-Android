package com.fobbu.member.android.activities.dashboard.presenter

interface DashboardHandler
{
    fun logout(userID:String,token:String)               //providing parameter of the update device token API to the presenter
}