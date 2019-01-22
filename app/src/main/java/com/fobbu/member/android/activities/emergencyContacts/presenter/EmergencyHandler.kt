package com.fobbu.member.android.activities.emergencyContacts.presenter

interface EmergencyHandler
{
    fun postEmergencyContracts(contactList:ArrayList<HashMap<String,String>>,token:String)
}