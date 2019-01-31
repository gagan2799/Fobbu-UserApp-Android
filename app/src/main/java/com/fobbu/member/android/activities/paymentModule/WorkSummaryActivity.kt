package com.fobbu.member.android.activities.paymentModule

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
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
class WorkSummaryActivity : AppCompatActivity(),ActivityView {


    private lateinit var rsaLiveHandler:RsaLivePresenter

    lateinit var workSummaryAdapter:WorkSummaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_summary)

        initView()

        clicks()
    }

    override fun onBackPressed() {

    }


    // method for handling all the initialization of this class
    private fun initView() {

        rsaLiveHandler=RsaLivePresenter(this,this)

        if (CommonClass(this,this).checkInternetConn(this))
        {
            rsaLiveHandler.getService(
                CommonClass(this,this).getString("x_access_token"),CommonClass(this, this).getString(
                RsaConstants.ServiceSaved.fobbuRequestId))
        }
    }



    // Method for handling click  functionality of the Class
    private fun clicks() {

        imageViewOptionMenuWorkSummary.setOnClickListener {
            showOptionLayout()
        }

        textViewConfirmAndPay.setOnClickListener {
            startActivity(Intent(this,PaymentModeActivity::class.java)
                .setFlags
            (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }


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

    override fun onRequestSuccessReport(mainPojo: MainPojo) {
        if (mainPojo.success=="true")
        {

            if(mainPojo.getData().addition_services.size>0)
            {
                setUpRecycler(mainPojo.getData().addition_services)
                var totalAmount: Long = 0

                for (i in mainPojo.getData().addition_services.indices) {
                    val noOfService: Long =
                        (mainPojo.getData().addition_services[i]["number_of_services"] as Double).toLong()
                    val servicePrice: Long =
                        (mainPojo.getData().addition_services[i]["service_price"] as Double).toLong()

                    totalAmount = totalAmount.plus(noOfService * servicePrice)
                }

                textViewTotalAmount.text = totalAmount.toString()

                textViewServiceTypeWork.text=(mainPojo.getData().service_name)

                textViewOrderIdWork.text=(mainPojo.getData().order_id)
            }
            else
            {
                textViewTotalAmount.text = (mainPojo.getData().total_price )

                textViewServiceTypeWork.text=(mainPojo.getData().service_name)

                textViewOrderIdWork.text=(mainPojo.getData().order_id)
            }

        }else{
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpRecycler(addition_services: ArrayList<HashMap<String, Any>>) {
        workSummaryAdapter= WorkSummaryAdapter(addition_services,this)
        recyclerViewWorkSummary.layoutManager=LinearLayoutManager(this)
        recyclerViewWorkSummary.adapter=workSummaryAdapter
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
