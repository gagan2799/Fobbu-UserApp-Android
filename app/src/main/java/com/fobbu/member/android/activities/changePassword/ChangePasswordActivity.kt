package com.fobbu.member.android.activities.changePassword

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.changePassword.presenter.PasswordHandler
import com.fobbu.member.android.activities.changePassword.presenter.PasswordPresenter
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar.*

class ChangePasswordActivity : AppCompatActivity(),ActivityView
{
    private lateinit var  commonClass: CommonClass

    private lateinit var passwordHandler:PasswordHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_password)

        initView()              //function for initialising all the variables of the class

        clicks()                 // function for handling all the clicks of the class
    }

    //function for initialising all the variables of the class
    fun initView()
    {
        commonClass= CommonClass(this,this)

        ivSearchToolbar.visibility= View.INVISIBLE

        passwordHandler=PasswordPresenter(this,this)
    }

    // function for handling all the clicks of the class
    fun clicks()
    {
        ivBackButton.setOnClickListener {
            finish()
        }

        tvReset.setOnClickListener {
            when
            {
                (etCurrentPasswordChange.text.trim().toString()!=commonClass.getString("user_password"))-> Toast.makeText(this,"Please provide correct current password.", Toast.LENGTH_SHORT).show()

                etPasswordChange.text.trim().isEmpty()->Toast.makeText(this,"Please provide password.", Toast.LENGTH_SHORT).show()

                etConfirmPass.text.trim().isEmpty()->Toast.makeText(this,"Please confirm the password.", Toast.LENGTH_SHORT).show()

                (etPasswordChange.text.trim().toString()== etCurrentPasswordChange.text.trim().toString())->Toast.makeText(this,"The new password is same as the old one.Please try another password.", Toast.LENGTH_SHORT).show()

                (etPasswordChange.text.trim().toString()!= etConfirmPass.text.toString())->Toast.makeText(this,"Password did not match. Please retry.", Toast.LENGTH_SHORT).show()

                else->
                    changePassword()                 //function for hitting change password API
            }
        }
    }

    //#########################CHANGE PASSWORD API##################//

    //function for hitting change password API
    private fun changePassword()
    {
        if (commonClass.checkInternetConn(this))
            passwordHandler.changePassword(etPasswordChange.text.toString(), commonClass.getString("x_access_token"))

        else
            Toast.makeText(this,resources.getString(R.string.internet_is_unavailable), Toast.LENGTH_SHORT).show()
    }

    // function for handling the response of the change password API
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success=="true")
        {
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()

            Handler().postDelayed({ finish() },1000)
        }
        else
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoader()
    {
        rlLoaderChangePass.visibility=View.VISIBLE

        aviPass.show()
    }

    override fun hideLoader()
    {
        rlLoaderChangePass.visibility=View.GONE

        aviPass.hide()
    }
}
