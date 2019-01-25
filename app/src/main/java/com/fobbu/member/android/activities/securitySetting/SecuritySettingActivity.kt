package com.fobbu.member.android.activities.securitySetting

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.changePassword.ChangePasswordActivity
import com.fobbu.member.android.activities.loginSignupModule.GeneratePINActivity
import kotlinx.android.synthetic.main.activity_security_setting.*
import kotlinx.android.synthetic.main.toolbar.*

class SecuritySettingActivity : AppCompatActivity()
{
    lateinit var commonClass:CommonClass
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_security_setting)

        initView()

        clicks()
    }


    // function for initialising all the variables of the class
    private fun initView()
    {
        commonClass=CommonClass(this,this)

        ivSearchToolbar.visibility=View.INVISIBLE
    }


    //function for handling the clicks of the class
    private fun clicks()
    {
        ivBackButton.setOnClickListener {
            finish()
        }

        llChangePassSecurity.setOnClickListener {
            startActivity(Intent(this,ChangePasswordActivity::class.java))
        }

        llPinSecurity.setOnClickListener {
            startActivity(Intent(this,GeneratePINActivity::class.java)
                .putExtra("pin","security"))
        }

    }


}
