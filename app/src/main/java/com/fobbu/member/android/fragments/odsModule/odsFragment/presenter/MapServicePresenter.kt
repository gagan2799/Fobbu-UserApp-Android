package com.fobbu.member.android.fragments.odsModule.odsFragment.presenter

import android.app.Activity
import android.widget.Toast
import com.fobbu.member.android.apiInterface.ApiClient
import com.fobbu.member.android.apiInterface.Handler.ResponseHandler
import com.fobbu.member.android.fragments.odsModule.odsFragment.view.MapServiceView
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.google.android.gms.maps.model.LatLng

class MapServicePresenter(var activity: Activity,var activityView: MapServiceView):MapServiceHandler
{
    // implementing the geocode/json API
    override fun getAddress(latlng: LatLng, sensor: String, key: String)
    {
        val apiClient=ApiClient(activity)

        val commonClass=CommonClass(activity,activity)

        apiClient.getAddress(latlng,sensor,key,object :ResponseHandler
        {
            override fun onSuccess(mainPojo: MainPojo)
            {
                activityView.onAddressSuccessReport(mainPojo)
            }

            override fun onError(message: String)
            {
              //  commonClass.showToast(message)
            }

            override fun onServerError(message: String)
            {
              //  commonClass.showToast(message)
            }

            override fun on401()
            {
               // commonClass.clearPreference()
            }
        })
    }
}