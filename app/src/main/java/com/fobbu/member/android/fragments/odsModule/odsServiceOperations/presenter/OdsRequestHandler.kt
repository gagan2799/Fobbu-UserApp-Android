package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.presenter

interface OdsRequestHandler
{
    // function for providing the parameter of the ods_requests API to the presenter
    fun makeOdsRequest(map:HashMap<String,Any>,token:String)
}