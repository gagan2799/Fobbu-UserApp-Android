package com.fobbu.member.android.fragments.rsaFragmentModule.presenter

interface FetchStatusHandler
{
    fun getServiceOneTime(token:String,requesID:String)    // providing parameters of the requests API to the presenter
}