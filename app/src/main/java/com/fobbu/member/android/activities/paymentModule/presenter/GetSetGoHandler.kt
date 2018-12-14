package com.fobbu.member.android.activities.paymentModule.presenter

import okhttp3.RequestBody

interface GetSetGoHandler {
     fun  postReviews(requestId:RequestBody,ratings:RequestBody,reviews:RequestBody,token:String)
}