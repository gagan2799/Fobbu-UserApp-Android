package com.fobbu.member.android.activities.emergencyContactsModule.contactList.presenter

interface ListHandler
{
    fun getContacts(token:String)                 // providing parameters of the GET emergencycontacts API to the presenter

    fun deleteContact(contactId:String,token: String)    // providing parameters of the DELETE emergencycontacts API to the presenter
}