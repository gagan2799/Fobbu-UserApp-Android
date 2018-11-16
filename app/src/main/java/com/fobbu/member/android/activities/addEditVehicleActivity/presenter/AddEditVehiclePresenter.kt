package com.fobbu.member.android.activities.addEditVehicleActivity.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddEditVehiclePresenter(internal  var activity:Activity,internal  var activityView: ActivityView):
AddEditActivityHandler
{
    override fun sendAddEditData(
        map: Map<String, RequestBody>,
        list: ArrayList<MultipartBody.Part>,
        tokenHeader: String
    ) {
        var apiClient= ApiClient(activity)
        apiClient.getAddEditVehicleData(map,list,tokenHeader,object :ResponseHandler
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