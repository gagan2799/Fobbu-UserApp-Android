package com.fobbu.member.android.activities.orderAndPassbookModule.passbookDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.orderAndPassbookModule.passbookDetail.adapter.PaymentDetailAdapter
import kotlinx.android.synthetic.main.activity_passbook_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class PassbookDetailActivity : AppCompatActivity()
{
    lateinit var commonClass:CommonClass

    private lateinit var paymentAdapter:PaymentDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passbook_detail)
        initView()

        clicks()

    }

    // function for handliing all the clicks of the class
    private fun clicks()
    {
        ivBackButton.setOnClickListener {
            finish()
        }
    }

    // function for initialising all the varaibles of the class
    private fun initView()
    {

        commonClass= CommonClass(this,this)

        ivSearchToolbar.visibility=View.INVISIBLE

        setRecycler()
    }


    // function for setting up the recycler
    private fun setRecycler()
    {
        paymentAdapter= PaymentDetailAdapter(this)

        rvPassDetail.layoutManager= LinearLayoutManager(this) as RecyclerView.LayoutManager?

        rvPassDetail.adapter=paymentAdapter
    }





}