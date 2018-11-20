package com.fobbu.member.android.activities.CancellationMessageActivity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import kotlinx.android.synthetic.main.activity_cancellation_message.*

class CancellationMessageActivity : AppCompatActivity() {

    val text:String= "Your request has been cancelled\n A Cancellation fee will be deducted \nfrom your fobbu wallet"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancellation_message)
        textViewCancellationRSA.text=text
        linearLayoutCancellation.setOnClickListener {
            startActivity(Intent(this,DashboardActivity::class.java))
            finish()
        }
    }
}
