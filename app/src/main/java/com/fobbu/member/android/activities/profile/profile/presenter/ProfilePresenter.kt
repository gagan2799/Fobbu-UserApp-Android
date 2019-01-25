package com.fobbu.member.android.activities.profile.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.activities.profile.view.ProfileView
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.view.ActivityView
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfilePresenter(var activity: Activity, var activityView: ActivityView,var profileView: ProfileView):ProfileHandler
{

    private val apiLClient=ApiClient(activity)

    override fun updateKyc(map: HashMap<String, Any>, token: String)
    {
        activityView.showLoader()

        apiLClient.updateKyc(map,token,object :ResponseHandler
        {
            override fun on401() {
                CommonClass(activity,activity).clearPreference()
            }

            override fun onSuccess(mainPojo: MainPojo) {
                activityView.hideLoader()

                profileView.updateKycSuccessReport(mainPojo)
            }

            override fun onError(message: String) {
                activityView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

            override fun onServerError(message: String) {
                activityView.hideLoader()

                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun updateUser(
        email: RequestBody,
        number: RequestBody,
        first: RequestBody,
        last: RequestBody,
        gender: RequestBody,
        file: ArrayList<MultipartBody.Part>
        ,token:String)

    {
        activityView.showLoader()

        apiLClient.updateUser(email,number,first,last,gender,file,token,object :ResponseHandler
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