package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.presenter

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView

class OdsRequestPresenter(var activity: Activity,var activityView: ActivityView) :OdsRequestHandler
{

    val apiClient=ApiClient(activity)

    override fun makeOdsRequest(map: HashMap<String, Any>, token: String) {
     activityView.showLoader()

        apiClient.makeOdsRequest(map,token,object :ResponseHandler
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

                println("server Error::$message")
            }

            override fun on401()
            {
            CommonClass(activity,activity).clearPreference()
            }

        })
    }

}