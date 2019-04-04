package com.fobbu.member.android.activities.paymentModule.presenter

interface PaymentModeHandler
{
    fun  makePayment(token:String,requestId:String)      // function for providing the parameters of the make_payment API to the presenter
}