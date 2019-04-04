package com.fobbu.member.android.activities.orderAndPassbookModule.MyOrder.presenter

interface OrderHandler
{
    fun getOrder(type:String,page:String,token:String)   //function for providing the parameters of the get_requests API to the presenter
}