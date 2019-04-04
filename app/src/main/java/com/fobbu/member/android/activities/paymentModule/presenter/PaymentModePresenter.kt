package com.fobbu.member.android.activities.paymentModule.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView

class PaymentModePresenter(private var activity: Activity,private var activityView: ActivityView):PaymentModeHandler
{
    // implementing the make_payment API
    override fun makePayment(token: String, requestId: String)
    {
        val apiClient=ApiClient(activity)

        apiClient.makePayment(token,requestId,object :ResponseHandler
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
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String)
            {
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}