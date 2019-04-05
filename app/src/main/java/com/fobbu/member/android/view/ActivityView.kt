package com.fobbu.member.android.view

import com.fobbu.member.android.modals.MainPojo

interface ActivityView
{
     // function for providing response of the API
     fun onRequestSuccessReport(mainPojo: MainPojo)

     // function for showing loader
     fun showLoader()

     // function for hiding loader
     fun hideLoader()
}