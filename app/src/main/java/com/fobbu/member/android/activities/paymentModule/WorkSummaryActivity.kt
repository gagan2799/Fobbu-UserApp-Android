package com.fobbu.member.android.activities.paymentModule

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.rsaModule.RSARequestCancelActivity
import kotlinx.android.synthetic.main.activity_work_summary.*
import kotlinx.android.synthetic.main.option_menu_layout.*

class WorkSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_summary)

        clicks()

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
}
