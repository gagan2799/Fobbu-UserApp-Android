package com.fobbu.member.android.view

import com.fobbu.member.android.modals.MainPojo

interface ActivityViewDashboard
{
     // function for providing the response of service API
     fun onRequestSuccessReportGetService(mainPojo: MainPojo)
}