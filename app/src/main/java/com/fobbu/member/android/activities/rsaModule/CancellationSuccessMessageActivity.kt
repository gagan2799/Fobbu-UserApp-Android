package com.fobbu.member.android.activities.rsaModule

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_cancellation_message.*

class CancellationSuccessMessageActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cancellation_message)

        initView()   // function for initialising all the variables and views of the class
    }

    // function for initialising all the variables and views of the class
    private fun initView()
    {
        textViewCancellationRSA.text=resources.getString(R.string.cancellation_by_user_msg)

        Handler().postDelayed({
            startActivity(Intent(this, DashboardActivity::class.java))

            finish()
        },1000)
    }
}
