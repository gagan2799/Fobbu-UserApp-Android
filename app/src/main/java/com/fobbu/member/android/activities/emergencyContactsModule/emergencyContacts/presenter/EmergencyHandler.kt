package com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.presenter

interface EmergencyHandler
{
    fun postEmergencyContracts(contactList:ArrayList<HashMap<String,String>>,token:String)
    fun editContacts(dataList:HashMap<String,Any>,id:String,token:String)
}