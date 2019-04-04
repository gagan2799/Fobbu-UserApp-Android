package com.fobbu.member.android.activities.paymentModule.presenter

import okhttp3.RequestBody

interface GetSetGoHandler
{
     // function for providing parameters of the provide_ratings API to the presenter
     fun  postReviews(requestId:RequestBody,ratings:RequestBody,reviews:RequestBody,token:String)
}