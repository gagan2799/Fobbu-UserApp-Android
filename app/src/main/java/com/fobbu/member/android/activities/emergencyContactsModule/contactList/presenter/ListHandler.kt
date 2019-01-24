package com.fobbu.member.android.activities.emergencyContactsModule.contactList.presenter

interface ListHandler {
    fun getContacts(token:String)
    fun deleteContact(contactId:String,token: String)
}