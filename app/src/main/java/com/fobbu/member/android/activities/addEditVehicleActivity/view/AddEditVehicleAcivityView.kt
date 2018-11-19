package com.fobbu.member.android.activities.addEditVehicleActivity.view

import com.fobbu.member.android.modals.MainPojo

interface AddEditVehicleAcivityView {
    fun onRequestSuccessReport(mainPojo: MainPojo)
    fun onRequestSuccessUpdateVehicle(mainPojo: MainPojo)
    fun showLoader()
    fun hideLoader()
}