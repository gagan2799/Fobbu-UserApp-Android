package com.fobbu.member.android.activities.signupActivity.presenter

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.fobbu.member.android.activities.signupActivity.SignUpActivity

import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView
import com.google.android.gms.auth.api.signin.internal.SignInHubActivity
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivityPresenter(var activity:Activity,var signUpActivityView: ActivityView):SignUpActivityHandler {

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