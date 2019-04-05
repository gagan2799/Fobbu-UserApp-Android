package com.fobbu.member.android.fragments.rsaFragmentModule.presenter

interface RsaLiveHandler
{
    // function for providing the parameters of the requests API to the presenter
    fun getService(token:String,requesID:String)
}