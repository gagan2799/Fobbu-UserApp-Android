package com.fobbu.member.android.activities.paymentModule

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.paymentModule.presenter.PaymentModeHandler
import com.fobbu.member.android.activities.paymentModule.presenter.PaymentModePresenter
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_payment_mode.*
import kotlinx.android.synthetic.main.option_menu_layout.*

class PaymentModeActivity : AppCompatActivity() , ActivityView{



    lateinit var paymentModeHandler:PaymentModeHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_mode)

        initView()
        clicks()
    }


    // method for initializing all the  variables of the class
    private fun initView() {
        paymentModeHandler=PaymentModePresenter(this,this)
    }



    // method for handling clicks of the class
    private fun clicks() {

        imageViewOptionMenuPaymentSummary.setOnClickListener {
            showOptionLayout()
        }

        linearLayoutBankPayment.setOnClickListener {

            if (CommonClass(this,this).checkInternetConn(this))
            {
                if (!CommonClass(this, this).getString("fobbu_request_id").isEmpty()) {
                    paymentModeHandler.makePayment(
                        CommonClass(this, this).getString("x_access_token"),
                        CommonClass(this, this).getString("fobbu_request_id")
                    )
                }
                    else {
                    Toast.makeText(this, getString(R.string.requestID_not_provided), Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this,getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
            }



        }

    }


    // method for showing option layout
    private fun showOptionLayout() {
        val dialog: Dialog = Dialog(this)
        dialog.setContentView(R.layout.option_menu_layout)
        var layoutParams= WindowManager.LayoutParams()
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
         startActivity(Intent(this,GetSetGoActivity::class.java)
             .setFlags
                 (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
     }else{
         Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
     }
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
