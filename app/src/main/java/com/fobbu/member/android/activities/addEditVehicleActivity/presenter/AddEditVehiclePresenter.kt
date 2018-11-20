package com.fobbu.member.android.activities.addEditVehicleActivity.presenter

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.fobbu.member.android.R.id.rlLoader
import com.fobbu.member.android.activities.addEditVehicleActivity.AddEditVehicleActivity
import com.fobbu.member.android.activities.addEditVehicleActivity.view.AddEditVehicleAcivityView
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_add_edit_vehicle.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddEditVehiclePresenter(internal  var activity:Activity,internal  var activityView: AddEditVehicleAcivityView):
    AddEditActivityHandler
{


    override fun findFobbuRequestUpdateVehicle(map: HashMap<String, String>, token: String) {

        var   apiClient=ApiClient(activity)
        activityView.showLoader()
        apiClient.findFobbuRequestUpdateVehicle(map,token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                activityView.hideLoader()
                activityView.onRequestSuccessUpdateVehicle(mainPojo)

            }

            override fun onError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity,"Error:"+message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity,"Server Error:"+message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun sendAddEditData(
        map: Map<String, RequestBody>,
        list: ArrayList<MultipartBody.Part>,
        tokenHeader: String
    ) {
        var apiClient= ApiClient(activity)
        activityView.showLoader()
        apiClient.getAddEditVehicleData(map,list,tokenHeader,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                activityView.hideLoader()
                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity,"Error:"+message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity,"Server Error:"+message, Toast.LENGTH_SHORT).show()
            }

        })

    }
}