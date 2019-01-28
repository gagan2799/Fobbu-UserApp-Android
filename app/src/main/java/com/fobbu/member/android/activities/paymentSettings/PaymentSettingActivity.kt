package com.fobbu.member.android.activities.paymentSettings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.toolbar.*

class PaymentSettingActivity : AppCompatActivity()
{
    lateinit var commonClass: CommonClass
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment_setting)

        initView()

        clicks()
    }

    //function for initialising all the variables of the class
    private fun initView()
    {
        commonClass= CommonClass(this,this)

        ivSearchToolbar.visibility= View.INVISIBLE
    }

    //function for handling all the clicks of the class
    fun clicks()
    {
        ivBackButton.setOnClickListener {
            finish()
        }
    }
}
