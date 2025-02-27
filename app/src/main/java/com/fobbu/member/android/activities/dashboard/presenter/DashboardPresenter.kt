package com.fobbu.member.android.activities.dashboard.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView

class DashboardPresenter(var activity:Activity,var activityView: ActivityView):DashboardHandler
{
    // implementing update device token API
    override fun logout(userID: String, token: String)
    {
        val apiClient=ApiClient(activity)

        apiClient.logout(userID,token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo)
            {
                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }
        })
    }
}