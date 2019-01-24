package com.fobbu.member.android.activities.emergencyContactsModule.contactList.view

import com.fobbu.member.android.modals.MainPojo

interface ContactListView {
    fun showLoader()
    fun hideLoader()
    fun SuccessReport(mainPojo: MainPojo)
    fun deleteContactSuccessReport( mainPojo: MainPojo)
}