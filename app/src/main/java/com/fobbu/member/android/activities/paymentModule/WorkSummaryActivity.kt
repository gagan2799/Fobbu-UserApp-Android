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
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.paymentModule.adapter.WorkSummaryAdapter
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.fragments.rsaFragmentModule.presenter.RsaLivePresenter
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_work_summary.*
import kotlinx.android.synthetic.main.option_menu_layout.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WorkSummaryActivity : AppCompatActivity(),ActivityView
{
    lateinit var commonClass:CommonClass

    private lateinit var rsaLiveHandler:RsaLivePresenter

    private lateinit var workSummaryAdapter:WorkSummaryAdapter

    lateinit var dataList:ArrayList<HashMap<String,Any>>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_work_summary)

        initView()                  // method for handling all the initialization of this class

        clicks()                    // Method for handling click  functionality of the Class
    }

    // method for handling all the initialization of this class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        recyclerViewWorkSummary.visibility=View.VISIBLE

        rsaLiveHandler=RsaLivePresenter(this,this)

        getService()               //implementing requests API
    }

    // Method for handling click  functionality of the Class
    private fun clicks()
    {
        imageViewOptionMenuWorkSummary.setOnClickListener {
            showOptionLayout()        // function for showing custom dialog
        }

        textViewConfirmAndPay.setOnClickListener {
            startActivity(Intent(this,PaymentModeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    // function for showing custom dialog
    @SuppressLint("RtlHardcoded")
    private fun showOptionLayout()
    {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.option_menu_layout)

        val layoutParams: WindowManager.LayoutParams

        layoutParams=dialog.window.attributes

        layoutParams.gravity= Gravity.TOP or Gravity.RIGHT

        layoutParams.x=-100

        layoutParams.y=-100

        layoutParams.windowAnimations=R.style.DialogTheme

        dialog.textViewCancelRSA.setOnClickListener {
            dialog.dismiss()

            startActivity(Intent(this, RSARequestCancelActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

            overridePendingTransition(R.anim.slide_down,R.anim.fade)
        }
        dialog.show()
    }

    //************************* requests API *********************//

    //implementing requests API
    private fun getService()
    {
        if (CommonClass(this,this).checkInternetConn(this))
            rsaLiveHandler.getService(CommonClass(this,this).getString("x_access_token"),CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId))

        else
            CommonClass(this,this).showToast(resources.getString(R.string.internet_is_unavailable),rlWorkSummary)
    }

    // handling response of the request API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            if(mainPojo.getData().addition_services.size>0)
            {
                setUpRecycler(mainPojo.getData().addition_services)          //function for setting up recycler

                var totalAmount: Long = 0

                for (i in mainPojo.getData().addition_services.indices)
                {
                    val noOfService: Long = (mainPojo.getData().addition_services[i]["number_of_services"] as Double).toLong()

                    val servicePrice: Long = (mainPojo.getData().addition_services[i]["service_price"] as Double).toLong()

                    totalAmount = totalAmount.plus(noOfService * servicePrice)
                }
                textViewTotalAmount.text = totalAmount.toString()

                textViewServiceTypeWork.text=(mainPojo.getData().service_name)

                textViewOrderIdWork.text=("#"+mainPojo.getData().order_id)
            }
            else
            {
                textViewTotalAmount.text = (mainPojo.getData().total_price )

                textViewServiceTypeWork.text=(mainPojo.getData().service_name)

                textViewOrderIdWork.text=("#"+mainPojo.getData().order_id)
            }
        }
        else
            CommonClass(this,this).showToast(mainPojo.message, rlWorkSummary)
    }

    //function for setting up recycler
    private fun setUpRecycler(addition_services: ArrayList<HashMap<String, Any>>)
    {
        workSummaryAdapter= WorkSummaryAdapter(addition_services,this)

        recyclerViewWorkSummary.layoutManager=LinearLayoutManager(this)

        recyclerViewWorkSummary.adapter=workSummaryAdapter

        recyclerViewWorkSummary.isNestedScrollingEnabled=false
    }

    override fun showLoader() {}

    override fun hideLoader() {}

    override fun onBackPressed()
    {
        val intent =   Intent(Intent.ACTION_MAIN)

        intent.addCategory(Intent.CATEGORY_HOME)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        startActivity(intent)
    }
}
