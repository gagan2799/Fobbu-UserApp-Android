package com.fobbu.member.android.activities.vehicleModule.presenter

interface VehicleListHandler {
     fun sendVehicleData(tokn:String,userid:String)
     fun deleteVehicle(token:String,vehicleId:String,userId:String)
}