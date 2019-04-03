package com.fobbu.member.android.activities.rsaModule

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_cancellation_message.*

class CancellationSuccessMessageActivity : AppCompatActivity() {

    val text:String= "Your request has been cancelled\n A Cancellation fee will be deducted \nfrom your fobbu wallet"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancellation_message)
        textViewCancellationRSA.text=text

        Handler().postDelayed({
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        },1000)
    }
}
