package com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView

class OrderPresenter(var activity: Activity, var activityView: ActivityView):OrderHandler
{
    override fun getOrder(type: String, page: String,token:String)
    {
    val apiClient=ApiClient(activity)

        activityView.showLoader()

        apiClient.getOrder(type,page,token,object :ResponseHandler
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