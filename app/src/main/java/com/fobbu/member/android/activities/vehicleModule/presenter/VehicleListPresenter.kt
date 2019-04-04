package com.fobbu.member.android.activities.vehicleModule.presenter

import android.app.Activity

import android.widget.Toast

import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView


class VehicleListPresenter(internal  var activity: Activity,internal var activityView: ActivityView):
    VehicleListHandler
{
    val apiClient= ApiClient(activity)

    // implementing  users/vehicles (GET) API
    override fun sendVehicleData(token: String, userid: String)
    {
        activityView.showLoader()

        apiClient.getVichleListData(token,userid,object :ResponseHandler
        {
            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }

            override fun onSuccess(mainPojo: MainPojo)
            {
                activityView.hideLoader()

                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                activityView.hideLoader()

                Toast.makeText(activity, "Error:$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                activityView.hideLoader()

                Toast.makeText(activity, "Server Error:$message", Toast.LENGTH_SHORT).show()
            }
        })
    }
}