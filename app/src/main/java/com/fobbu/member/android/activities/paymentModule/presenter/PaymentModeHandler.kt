package com.fobbu.member.android.activities.paymentModule.presenter

interface PaymentModeHandler {
    fun  makePayment(token:String,requestId:String)
}