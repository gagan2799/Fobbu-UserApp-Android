package com.fobbu.member.android.activities.paymentModule

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.fobbu.member.android.R
import com.fobbu.member.android.R.id.tvTotalAmount
import com.fobbu.member.android.activities.paymentModule.adapter.OdsWorkAdapter
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_ods_work_summary.*
import kotlinx.android.synthetic.main.activity_work_summary.*
import kotlinx.android.synthetic.main.option_menu_layout.*

class OdsWorkSummaryActivity : AppCompatActivity()
{
    lateinit var dataList:ArrayList<HashMap<String,Any>>

    lateinit var commonClass:CommonClass

    private var selectedServiceList=ArrayList<HashMap<String,Any>>()

    lateinit var odsWorkAdapter: OdsWorkAdapter

    private var odsServiceTime=""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ods_work_summary)

        initView()

        clicks()
    }



    override fun onBackPressed() {
    }

    // function for initialising all the variables of the class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        dataList= ArrayList()

        if (intent.hasExtra(RsaConstants.Ods.selectedServiceList))
            selectedServiceList= intent.getSerializableExtra(RsaConstants.Ods.selectedServiceList) as ArrayList<HashMap<String, Any>>

        ivOptionMenuWorkSummary.visibility= View.INVISIBLE

        if (commonClass.getStringList(RsaConstants.Ods.singleServiceList).isNotEmpty())
            dataList=commonClass.getStringList(RsaConstants.Ods.singleServiceList)

        tvServiceTypeWork.text=dataList[0][RsaConstants.Ods.service_name].toString()

        tvTotalAmount.text=(dataList[0][RsaConstants.Ods.service_price] as Double).toLong().toString()

        odsServiceTime=intent.getStringExtra(RsaConstants.Ods.time)

        llOdsService.visibility= View.VISIBLE

        tvAddressWorkSummary.text=commonClass.getString(RsaConstants.Ods.address)

        tvTimeWorkSummary.text=intent.getStringExtra(RsaConstants.Ods.time)

        tvDateWorkSummary.text=intent.getStringExtra(RsaConstants.Ods.date)

        setUpRecyler()
    }

    // function for handling clicks of the class
    private fun clicks()
    {
        tvConfirmAndPay.setOnClickListener {
            startActivity(
                Intent(this,PaymentModeActivity::class.java)
                    .setFlags
                        (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(RsaConstants.Ods.service_name,odsServiceTime))
        }

        ivOptionMenuWorkSummary.setOnClickListener {
            showOptionLayout()
        }
    }

    //function for setting up recycler
    private fun setUpRecyler()
    {
        odsWorkAdapter= OdsWorkAdapter(this,selectedServiceList)

        rvOdsWorkSummary.layoutManager=LinearLayoutManager(this)

        rvOdsWorkSummary.isNestedScrollingEnabled=false

        rvOdsWorkSummary.adapter=odsWorkAdapter
    }


    // function for showing layout for sharing
    @SuppressLint("RtlHardcoded")
    private fun showOptionLayout() {
        val dialog: Dialog = Dialog(this)
        dialog.setContentView(R.layout.option_menu_layout)
        val layoutParams: WindowManager.LayoutParams
        layoutParams=dialog.window.attributes
        layoutParams.gravity= Gravity.TOP or Gravity.RIGHT
        layoutParams.x=-100
        layoutParams.y=-100
        layoutParams.windowAnimations=R.style.DialogTheme

        dialog.textViewCancelRSA.setOnClickListener {
            dialog.dismiss()
            startActivity(
                Intent(this, RSARequestCancelActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            overridePendingTransition(R.anim.slide_down,R.anim.fade)
        }
        dialog.show()
    }
}
