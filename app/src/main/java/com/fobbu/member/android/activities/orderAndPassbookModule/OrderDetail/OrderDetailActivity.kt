package com.fobbu.member.android.activities.orderAndPassbookModule.OrderDetail

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.orderAndPassbookModule.OrderDetail.adapter.OrderDetailAdapter
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class OrderDetailActivity : AppCompatActivity()
{
    private lateinit var commonClass:CommonClass

    private var dataList=ArrayList<HashMap<String,Any>>()

    private var servicesList=ArrayList<HashMap<String,Any>>()

    private lateinit var orderDetailAdapter: OrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_order_detail)

        initView()

        clicks()
    }

    // function for initialising all the views of the class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        if (intent.hasExtra("service_list"))
        {
            dataList=intent.getStringArrayListExtra("service_list") as ArrayList<HashMap<String, Any>>
        }

        setDataInView()

        ivSearchToolbar.visibility= View.INVISIBLE

        setRecycler()
    }

    //function for handling all the clicks of the class
    private fun clicks()
    {
        ivBackButton.setOnClickListener {
            finish()
        }
    }


    // function for setting up recycler view
    private fun setRecycler()
    {
        orderDetailAdapter= OrderDetailAdapter(this,servicesList)

        rvorderDetail.layoutManager= LinearLayoutManager(this)

        rvorderDetail.adapter=orderDetailAdapter
    }


    // function for setting up data from the list into the view
    @SuppressLint("SetTextI18n")
    private fun setDataInView()
    {
        tvOrderIDDetail.text= """${resources.getString(R.string.order_id)}${(dataList[0]["order_id"] as Double).toLong()}"""

        tvOrderDetailTime.text=commonClass.getDesireFormat("yyyy-MM-dd'T'HH:mm:ss.SSS","HH:mm aa dd MMM yyyy",dataList[0]["created"].toString())

        tvOrderDetailService.text= dataList[0]["service_name"].toString()

        tvOrderDetailBelowTotal.text= resources.getString(R.string.rs)+(dataList[0]["total_price"] as Double).toLong()

        for (i in dataList.indices)
        {
            servicesList= dataList[i]["addition_services"] as ArrayList<HashMap<String, Any>>
        }
    }
}
