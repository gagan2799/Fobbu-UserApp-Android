package com.fobbu.member.android.activities.vehicleListActivity.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView

class VehicleListPresenter(internal  var activity: Activity,internal var activityView: ActivityView):VehicleListHandler {
    override fun sendVehicleData(token: String, userid: String) {
        var apiClient= ApiClient(activity)
        apiClient.getVichleListData(token,userid,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                Toast.makeText(activity,"Error:"+message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                Toast.makeText(activity,"Server Error:"+message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}