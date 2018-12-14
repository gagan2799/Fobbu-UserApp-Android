package com.fobbu.member.android.activities.vehicleModule.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditActivityHandler
import com.fobbu.member.android.activities.vehicleModule.view.AddEditVehicleAcivityView
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddEditVehiclePresenter(internal  var activity:Activity,internal  var activityView: AddEditVehicleAcivityView):
    AddEditActivityHandler
{


    override fun findFobbuRequestUpdateVehicle(map: HashMap<String, String>, token: String) {

        val apiClient=ApiClient(activity)
        activityView.showLoader()
        apiClient.findFobbuRequestUpdateVehicle(map,token,object :ResponseHandler
        {

            override fun on401() {
                CommonClass(activity,activity).clearPreference()
            }

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
        val apiClient= ApiClient(activity)
        activityView.showLoader()
        apiClient.getAddEditVehicleData(map,list,tokenHeader,object :ResponseHandler
        {

            override fun on401() {
                CommonClass(activity,activity).clearPreference()
            }

            override fun onSuccess(mainPojo: MainPojo) {
                activityView.hideLoader()
                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity, "Error:$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity, "Server Error:$message", Toast.LENGTH_SHORT).show()
            }

        })
    }


    override fun sendEditData(
        map: Map<String, RequestBody>,
        list: ArrayList<MultipartBody.Part>,
        tokenHeader: String
    ) {
        val apiClient= ApiClient(activity)
        activityView.showLoader()
        apiClient.getEditVehicleData(map,list,tokenHeader,object :ResponseHandler
        {

            override fun on401() {
                CommonClass(activity,activity).clearPreference()
            }

            override fun onSuccess(mainPojo: MainPojo) {
                activityView.hideLoader()
                activityView.onRequestSuccessReportEdit(mainPojo)
            }

            override fun onError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity, "Error:$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                activityView.hideLoader()
                Toast.makeText(activity, "Server Error:$message", Toast.LENGTH_SHORT).show()
            }

        })

    }



    // method for handling api of deleting vehicle
    override fun deleteVehicle(token: String, vehicleId: String, userId: String) {
        val apiClient= ApiClient(activity)
        apiClient.deleteVehicle(token,vehicleId,userId,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                activityView.onDeleteVehicleSuccessUpdateVehicle(mainPojo)
            }

            override fun onError(message: String) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }

            override fun on401() {
                CommonClass(activity,activity).clearPreference()
            }

        })
    }


}