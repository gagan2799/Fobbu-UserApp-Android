package com.fobbu.member.android.activities.vehicleModule.presenter

interface VehicleListHandler
{
     // function for providing the parameters of the users/vehicles (GET) API to the presenter
     fun sendVehicleData(tokn:String,userid:String)
}