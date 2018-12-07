package com.fobbu.member.android.fragments.rsaFragmentModule.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView

class RsaLivePresenter (private var activity:Activity,private var activityView: ActivityView):RsaLiveHandler{


    override fun getService(token:String,requesID: String) {
        val apiClient= ApiClient(activity)
        apiClient.getService(token,requesID,object :ResponseHandler{
            override fun onSuccess(mainPojo: MainPojo) {
                activityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                Toast.makeText(activity, "Error :$message", Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}