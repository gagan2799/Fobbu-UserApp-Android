package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.presenter

interface OdsRequestHandler {
    fun makeOdsRequest(map:HashMap<String,Any>,token:String)
}