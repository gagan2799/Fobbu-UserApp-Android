package com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.presenter

interface EmergencyHandler
{
    fun postEmergencyContracts(contactList:ArrayList<HashMap<String,String>>,token:String)    // function for providing parameters of the emergencycontacts API (POST) to the presenter

    fun editContacts(dataList:HashMap<String,Any>,id:String,token:String)    // function for providing parameters of the emergencycontacts API (PUT) to the presenter
}