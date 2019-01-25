package com.fobbu.member.android.activities.loginSignupModule.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.activities.loginSignupModule.SignUpActivity

import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView


class SignUpActivityPresenter(var activity:Activity,var signUpActivityView: ActivityView):
    SignUpActivityHandler {

    val signupLoader= SignUpActivity()
    override fun sendSignUpData(
        user_type: String,
        firstName: String,
        lastName: String,
        displayName: String,
        email: String,
        password: String,
        mobile: String,
        gender: String,
        token: String
    ) {
        var apiClient= ApiClient(activity)
        signUpActivityView.showLoader()
        apiClient.getSignupData(user_type,firstName,lastName,displayName,email,password,mobile,gender,token,object :ResponseHandler
        {
            override fun on401() {
                CommonClass(activity,activity).clearPreference()
            }

            override fun onSuccess(mainPojo: MainPojo) {
                signUpActivityView.hideLoader()
                signUpActivityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                signUpActivityView.hideLoader()
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                signUpActivityView.hideLoader()
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}