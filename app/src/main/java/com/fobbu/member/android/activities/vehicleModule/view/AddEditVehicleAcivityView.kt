package com.fobbu.member.android.activities.vehicleModule.view

import com.fobbu.member.android.modals.MainPojo

interface AddEditVehicleAcivityView {
    fun onRequestSuccessReport(mainPojo: MainPojo)
    fun onRequestSuccessReportEdit(mainPojo: MainPojo)
    fun onRequestSuccessUpdateVehicle(mainPojo: MainPojo)
    fun showLoader()
    fun hideLoader()
}