package com.fobbu.member.android.activities.emergencyContactsModule.contactList.view

import com.fobbu.member.android.modals.MainPojo

interface ContactListView
{
    fun showLoader()          // function for showing loader

    fun hideLoader()          // function for hiding loader

    fun SuccessReport(mainPojo: MainPojo)     // function for providing response of the emergencycontacts API (GET)

    fun deleteContactSuccessReport( mainPojo: MainPojo)            // function for providing response of the emergencycontacts API (DELETE)
}