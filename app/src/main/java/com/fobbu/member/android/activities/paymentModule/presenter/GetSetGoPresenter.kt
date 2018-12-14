package com.fobbu.member.android.activities.paymentModule.presenter

import android.app.Activity
import android.media.session.MediaSession
import android.widget.Toast
import com.fobbu.member.android.activities.vehicleModule.view.AddEditVehicleAcivityView
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import okhttp3.RequestBody

class GetSetGoPresenter(var activity: Activity,var acivityView: ActivityView):GetSetGoHandler {

    override fun postReviews(requestId: RequestBody, ratings: RequestBody, reviews: RequestBody,token: String) {
    val apiClient=ApiClient(activity)
        apiClient.postReviews(requestId,ratings,reviews,token,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo) {
                acivityView.onRequestSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun on401() {
                CommonClass(activity,activity).clearPreference()
            }

        })
    }
}