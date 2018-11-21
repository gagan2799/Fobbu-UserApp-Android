package com.fobbu.member.android.activities.loginSignupModule.presenter

import android.app.Activity
import android.widget.Toast

import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView

class LoginActivityPresenter(var activity :Activity, var loginActivityView: ActivityView):
    LoginActivityHandler {

    var apiClient= ApiClient(activity)

    override fun sendLoginData(mobile: String, password: String, token: String) {
        loginActivityView.showLoader()
        apiClient.getLoginData(mobile,password,token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                loginActivityView.hideLoader()
                loginActivityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                loginActivityView.hideLoader()
                Toast.makeText(activity,""+message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                loginActivityView.hideLoader()
                Toast.makeText(activity,""+message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}