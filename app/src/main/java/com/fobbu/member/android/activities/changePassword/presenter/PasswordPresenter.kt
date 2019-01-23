package com.fobbu.member.android.activities.changePassword.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView

class PasswordPresenter(var activity: Activity,var activityView: ActivityView):PasswordHandler
{
    override fun changePassword(password:String,token: String)
    {
        activityView.showLoader()

        val apiClient= ApiClient(activity)

        apiClient.changePassword(password,token,object :ResponseHandler
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