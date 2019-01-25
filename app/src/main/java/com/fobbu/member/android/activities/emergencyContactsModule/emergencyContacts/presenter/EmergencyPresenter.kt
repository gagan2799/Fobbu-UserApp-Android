package com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.presenter

import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import com.fobbu.member.android.activities.emergencyContactsModule.emergencyContacts.view.ContactsView
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView

class EmergencyPresenter(var activity: Activity,var activityView: ActivityView,var contactsView: ContactsView):EmergencyHandler
{
    private val apiCLient=ApiClient(activity)

    override fun editContacts(dataList: HashMap<String, Any>, id: String, token: String)
    {
        activityView.showLoader()

        apiCLient.editContacts(dataList,token,id,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo)
            {
                activityView.hideLoader()

                contactsView.editContactsSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                activityView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                activityView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun postEmergencyContracts(contactList: ArrayList<HashMap<String, String>>, token: String)
    {
        activityView.showLoader()

        apiCLient.postEmergencyContacts(contactList,token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo)
            {
                activityView.hideLoader()

             activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                activityView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                activityView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }

        })
    }
}