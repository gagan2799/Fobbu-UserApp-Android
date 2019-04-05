package com.fobbu.member.android.fragments.odsModule.odsFragment.presenter

import com.google.android.gms.maps.model.LatLng

interface MapServiceHandler
{
    fun getAddress(latlng:LatLng,sensor:String,key:String)   // providing the parameters of the geocode/json to the presenter
}