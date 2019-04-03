package com.fobbu.member.android.activities.emergencyContactsModule.contactList.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.activities.emergencyContactsModule.contactList.view.ContactListView
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass


class ListPresenter(var activity: Activity,var contactView:ContactListView):ListHandler
{
    val apiClient= ApiClient(activity)

    // implementing  emergencycontacts API (DELETE)
    override fun deleteContact(contactId: String, token: String)
    {
        apiClient.deleteContact(contactId,token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo)
            {
                contactView.hideLoader()

                contactView.deleteContactSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                contactView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                contactView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }
        })
    }

    // implementing  emergencycontacts API (GET)
    override fun getContacts(token: String)
    {
        contactView.showLoader()

        apiClient.getContact(token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo)
            {
                contactView.hideLoader()

                contactView.SuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                contactView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                contactView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }
        })
    }
}