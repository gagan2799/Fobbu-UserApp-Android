package com.fobbu.member.android.fragments.odsModule.odsFragment.view

import com.fobbu.member.android.modals.MainPojo

interface MapServiceView
{
    fun onAddressSuccessReport(mainPojo: MainPojo)       // function for providing the response of the  geocode/json API
}