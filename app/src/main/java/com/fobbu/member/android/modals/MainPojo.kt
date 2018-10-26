package com.fobbu.member.android.modals

import java.util.ArrayList
import java.util.HashMap

class MainPojo
{
    private  var data = Data()

    fun setData(data: Data) {
        this.data = data
    }

    fun getData(): Data {
        return data
    }


    var message: String = ""

    var pin: String = ""

    var lat_long: String = ""

    var success: String = ""

    var total_earning: String = ""

    var client_token : String = ""

    var list: ArrayList<HashMap<String, Any>> = ArrayList()
}