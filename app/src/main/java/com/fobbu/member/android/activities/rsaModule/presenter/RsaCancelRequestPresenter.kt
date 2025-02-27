package com.fobbu.member.android.activities.rsaModule.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView

class RsaCancelRequestPresenter (private var activity:Activity,private var activityView: ActivityView):RsaCancelRequestHandler
{
    // implementing change_status API for cancellation
    override fun cancelRequest(reason: String, requestID: String,token:String)
    {
        val apiClient=ApiClient(activity)

        apiClient.cancellationRequest(reason,requestID,token,object :ResponseHandler
        {
            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }

            override fun onSuccess(mainPojo: MainPojo)
            {
                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                Toast.makeText(activity, "Error :$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                Toast.makeText(activity, "Server Error :$message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // implementing reason?type=user API
    override fun cancelReasons(token: String)
    {
        val  apiClient=ApiClient(activity)

        apiClient.cancelReasons(token,object :ResponseHandler
        {
            override fun on401()
            {
                CommonClass(activity,activity).clearPreference()
            }

            override fun onSuccess(mainPojo: MainPojo)
            {
                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
                Toast.makeText(activity, "Error :$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                Toast.makeText(activity, "Server Error :$message", Toast.LENGTH_SHORT).show()
            }

        })
    }
}