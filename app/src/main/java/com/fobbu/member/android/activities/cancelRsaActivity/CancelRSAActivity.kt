package com.fobbu.member.android.activities.cancelRsaActivity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.cancellationMessageActivity.CancellationMessageActivity
import kotlinx.android.synthetic.main.activity_cancel_rsa.*
import kotlinx.android.synthetic.main.fragment_builder_confirm.view.*

class CancelRSAActivity : AppCompatActivity() {

    val text:String="Are you sure you want to cancel\n A cancelation fee or Rs$$ will be charged"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_rsa)
        ivBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade,R.anim.slide_up)
        }
        buttonCancelRequestRSA.setOnClickListener {
            showPopUp(this)
        }
       /* val colorDrawable= ColorDrawable(Color.TRANSPARENT)
        window.setBackgroundDrawable(colorDrawable)*/


    }

    private fun showPopUp(activity: Activity) {
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val cViewFinalPopup = inflater.inflate(R.layout.fragment_builder_confirm, null)

        val builderFinal = Dialog(activity!!)

        builderFinal.requestWindowFeature(Window.FEATURE_NO_TITLE)

        builderFinal.setCancelable(false)

        builderFinal.setContentView(cViewFinalPopup)

        builderFinal.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val tvCancel = cViewFinalPopup.findViewById(R.id.tvCancel) as TextView

        val tvText = cViewFinalPopup.findViewById(R.id.tvText) as TextView
        tvText.text =text
        cViewFinalPopup.tvCancel.text="NO"
        cViewFinalPopup.tvConfirm.text="YES"

        val tvConfirm = cViewFinalPopup.findViewById(R.id.tvConfirm) as TextView

        tvCancel.setOnClickListener {
            builderFinal.dismiss()

        }
        tvConfirm.setOnClickListener {
            builderFinal.dismiss()
            startActivity(Intent(this,
                CancellationMessageActivity::class.java))
            finish()
            //
        }

        builderFinal.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        builderFinal.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.fade,R.anim.slide_up)
    }
}
