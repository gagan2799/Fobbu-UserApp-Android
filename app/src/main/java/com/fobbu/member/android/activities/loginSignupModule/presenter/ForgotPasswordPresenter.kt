package com.fobbu.member.android.activities.loginSignupModule.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView

class ForgotPasswordPresenter (internal var activity:Activity,internal var activityView:ActivityView):
    ForgotPasswordHandler
{
    // implementing  forgot-password API
    override fun getPassword(email: String)
    {
        val apiClient= ApiClient(activity)

        activityView.showLoader()

        apiClient.forgotPassword(email,object :ResponseHandler
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