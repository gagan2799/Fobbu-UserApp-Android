package com.fobbu.member.android.view

import com.fobbu.member.android.modals.MainPojo

interface ActivityView {
     fun onRequestSuccessReport(mainPojo: MainPojo)
     fun showLoader()
     fun hideLoader()
}