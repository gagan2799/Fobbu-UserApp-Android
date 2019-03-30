package com.fobbu.member.android.fcm

class FcmPushTypes
{
    object Types {
        var accept = "accepted"
        var acceptRequestBroadCast = "accept_request_broadcast"
        var inRouteRequest = "in_route"
        var inRouteRequestBroadCast = "in_route_request_broadcast"
        var fromAPIBroadCast = "from_api_request_broadcast"
        var newPin = "new_pin"
        var otpVerified = "otp_verified"
        var otpGenerated="otp_generated"
        var moneyRequested = "money_requested"
        var moneyPaid = "money_paid"
        var moneyRequestedBroadcast="money_requested_broadcast"
        var startedWork="in_progress"
        var startedWorkBroadcast="in_progress_started_work"
        var workEnded="completed"
        var reviewAdded="review_added"
        var requestCancelled="cancelled_by_partner"
        var cancelledByAdmin="cancelled_by_admin"
        var fuelDefaultScreen="fuel_default"
        var checkStatusPushOneTime="check_Status_Push_One_Time"
        var clearDataNavigateToHomeScreen="clear_Preference"
    }
}