package com.fobbu.member.android.fragments.rsaFragmentModule.view

import com.fobbu.member.android.modals.MainPojo

interface RsaFragmentView {
    fun fetchingServiceReport(mainPojo: MainPojo)
    fun findingFobbuReport(mainPojo: MainPojo)
    fun fleetSuccessReport(mainPojo: MainPojo)
    fun showLoader()
    fun hideLoader()
}