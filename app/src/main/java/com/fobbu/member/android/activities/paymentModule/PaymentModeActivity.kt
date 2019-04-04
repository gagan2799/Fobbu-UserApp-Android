package com.fobbu.member.android.activities.paymentModule

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.DashboardActivity
import com.fobbu.member.android.activities.paymentModule.presenter.PaymentModeHandler
import com.fobbu.member.android.activities.paymentModule.presenter.PaymentModePresenter
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_payment_mode.*
import kotlinx.android.synthetic.main.inflate_review_pop_up.view.*
import kotlinx.android.synthetic.main.option_menu_layout.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PaymentModeActivity : AppCompatActivity(), ActivityView
{
    private lateinit var paymentModeHandler: PaymentModeHandler

    lateinit var commonClass:CommonClass

    var staticName=""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment_mode)

        initView()             // method for initializing all the  variables of the class

        clicks()                 // method for handling clicks of the class
    }

    // method for initializing all the  variables of the class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        paymentModeHandler = PaymentModePresenter(this, this)
    }

    // method for handling clicks of the class
    private fun clicks()
    {
        imageViewOptionMenuPaymentSummary.setOnClickListener {
            showOptionLayout()
        }

        linearLayoutBankPayment.setOnClickListener {
            if (intent.hasExtra(RsaConstants.Ods.service_name))
            {
                successDialog()      // function for showing dialog with success message

                if (commonClass.getStringList(RsaConstants.Ods.singleServiceList).isNotEmpty())
                    staticName=commonClass.getStringList(RsaConstants.Ods.singleServiceList)[0][RsaConstants.Ods.static_name].toString()

                Handler().postDelayed({
                    startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(RsaConstants.Ods.static_name,staticName))
                },1000)
            }
            else
                makePayment()         // implementing the make_payment API
        }

    }

    // function for showing dialog with success message
    private fun successDialog()
    {
        val builder=AlertDialog.Builder(this)

        val view: View = LayoutInflater.from(this).inflate(R.layout.inflate_review_pop_up, null)

        builder.setView(view)

        val dialog= builder.create()

        view.tvReviewMessage.text=resources.getString(R.string.service_booked_message)

        view.imageViewrFacebookReview.visibility=View.GONE

        view.imageViewrTwitterReview.visibility=View.GONE

        dialog.show()
    }

    // method for showing option layout
    private fun showOptionLayout()
    {
        val dialog: Dialog = Dialog(this)

        dialog.setContentView(R.layout.option_menu_layout)

        var layoutParams = WindowManager.LayoutParams()

        layoutParams = dialog.window.attributes

        layoutParams.gravity = Gravity.TOP or Gravity.RIGHT

        layoutParams.x = -100

        layoutParams.y = -100

        layoutParams.windowAnimations = R.style.DialogTheme

        dialog.textViewCancelRSA.setOnClickListener {
            dialog.dismiss()

            startActivity(Intent(this, RSARequestCancelActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

            overridePendingTransition(R.anim.slide_down, R.anim.fade)
        }
        dialog.show()
    }

    //***************************** make_payment API *************************//

    // implementing the make_payment API
    private fun makePayment()
     {
         if (CommonClass(this, this).checkInternetConn(this))
         {
             if (!CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId).isEmpty())
                 paymentModeHandler.makePayment(CommonClass(this, this).getString("x_access_token"), CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId))

             else
                 Toast.makeText(this, getString(R.string.requestID_not_provided), Toast.LENGTH_SHORT).show()
         }
         else
             Toast.makeText(this, getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
     }

    // handling response of the make_payment API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            CommonClass(this, this).putString(RsaConstants.RsaTypes.checkStatus, FcmPushTypes.Types.moneyPaid)

            if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                RsaConstants.ServiceName.flatTyre
                ||
                CommonClass(this, this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                RsaConstants.ServiceName.burstTyre
            )
            {
                startActivity(Intent(this, GetSetGoActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

                finish()
            }
            else
            {
                CommonClass(this,this).putString(RsaConstants.RsaTypes.checkStatus,FcmPushTypes.Types.moneyPaid)

                startActivity(Intent(this, DashboardActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

                CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")

                CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreenType, resources.getString(R.string.rsa_live))

                finish()
            }
        }
        else
            Toast.makeText(this, mainPojo.message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoader() {}

    override fun hideLoader() {}
}
