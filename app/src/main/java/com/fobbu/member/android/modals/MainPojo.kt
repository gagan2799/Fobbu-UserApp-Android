package com.fobbu.member.android.modals

import java.util.ArrayList
import java.util.HashMap

class MainPojo
{
    private  var data = Data()

      var urls =Urls()

    fun setData(data: Data) {
        this.data = data
    }

    fun getData(): Data {
        return data
    }

    var results=ArrayList<HashMap<String,Any>>()

    var message: String = ""

    var error_message=""

    var pin: String = ""

    var token: String = ""



    var success: String = ""

    var total_earning: String = ""

    var client_token : String = ""

    var list: ArrayList<HashMap<String, Any>> = ArrayList()

    var services=ArrayList<HashMap<String,Any>>()

    var vehicles=ArrayList<HashMap<String,Any>>()

    var active_requests=ArrayList<HashMap<String,Any>>()
}