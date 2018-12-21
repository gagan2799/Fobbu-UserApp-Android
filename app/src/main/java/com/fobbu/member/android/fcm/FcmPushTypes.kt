package com.fobbu.member.android.fcm

class FcmPushTypes
{
    object Types {
        var accept = "accept_request"
        var acceptRequestBroadCast = "accept_request_broadcast"
        var inRouteRequest = "in_route_request"
        var inRouteRequestBroadCast = "in_route_request_broadcast"
        var newPin = "new_pin"
        var otpVerified = "otp_verified"
        var moneyRequested = "money_requested"
        var moneyRequestedBroadcast="money_requested_broadcast"
        var startedWork="in_progress"
        var startedWorkBroadcast="in_progress_started_work"
        var workEnded="completed"
        var requestCancelled="cancelled_by_partner"
    }
}