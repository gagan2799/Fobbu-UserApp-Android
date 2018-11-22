package com.fobbu.member.android.fragments.rsaFragmentModule.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.fragments.rsaFragmentModule.view.RsaFragmentView
import com.fobbu.member.android.modals.MainPojo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RsaFragmnetPresenter(private var activity: Activity,private var rsaFragmentView: RsaFragmentView):RsaFragmentHandler {


    var apiClient=ApiClient(activity)


    // fetch service api
    override fun fetchService(token: String) {
        rsaFragmentView.showLoader()
    apiClient.fetchService(token,object :ResponseHandler
    {
        override fun onSuccess(mainPojo: MainPojo) {
            rsaFragmentView.hideLoader()
            rsaFragmentView.fetchingServiceReport(mainPojo)
        }

        override fun onError(message: String) {
            rsaFragmentView.hideLoader()
            Toast.makeText(activity,"Error:$message", Toast.LENGTH_SHORT).show()
        }

        override fun onServerError(message: String) {
            rsaFragmentView.hideLoader()
            Toast.makeText(activity,"Server Error:$message", Toast.LENGTH_SHORT).show()
        }

    })
    }



    // find fobbu request api
    override fun findFobbuRequest(
        userId: RequestBody,
        serviceSelected: RequestBody,
        strtLatitude: RequestBody,
        strLongitude: RequestBody,
        strVehicleType: RequestBody,
        fileList: ArrayList<MultipartBody.Part>,
        token: String
    ) {
        rsaFragmentView.showLoader()
        apiClient.findFobbuRequest(userId,serviceSelected,strtLatitude,strLongitude,strVehicleType,fileList,token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                rsaFragmentView.hideLoader()
                rsaFragmentView.findingFobbuReport(mainPojo)
            }

            override fun onError(message: String) {
                rsaFragmentView.hideLoader()
                Toast.makeText(activity,"Error:$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                rsaFragmentView.hideLoader()
                Toast.makeText(activity,"Server Error:$message", Toast.LENGTH_SHORT).show()
            }

        })
    }


    // find fleet or user api
    override fun findFleetOrUser(token: String, requestId: String) {
        apiClient.findFleetOrUser(token,requestId,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                rsaFragmentView.fleetSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                Toast.makeText(activity,"Error:$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                Toast.makeText(activity,"Server Error:$message", Toast.LENGTH_SHORT).show()
            }

        })
    }

}