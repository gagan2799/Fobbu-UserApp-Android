package com.fobbu.member.android.activities.loginSignupModule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginSignupModule.presenter.ForgotPasswordHandler
import com.fobbu.member.android.activities.loginSignupModule.presenter.ForgotPasswordPresenter
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_forgot.*

class ForgotPasswordActivity : AppCompatActivity(),ActivityView
{
    private lateinit var webServiceApi: WebServiceApi

    lateinit var forgotPasswordHandler: ForgotPasswordHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgot)

        initView()    // function for initialising the variables and views of the class

        addClicks()     // All the clicks of this activity are handled in this method
    }

    // function for initialising the variables and views of the class
    private fun initView()
    {
        forgotPasswordHandler= ForgotPasswordPresenter(this, this)
    }

    // All the clicks of this activity are handled in this method
    private fun addClicks()
    {
        webServiceApi = getEnv().getRetrofit()

        ivBack.setOnClickListener { finish() }

        tvReset.setOnClickListener {
            when
            {
                etEmail.text.trim().isEmpty() ->
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_email),rlForgot)

                !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim()).matches() ->
                    CommonClass(this, this).showToast(resources.getString(R.string.correct_email_error),rlForgot)

                else ->
                    callForgotAPIUser(etEmail.text.toString().trim())              // Forgot Password API (API-users/forgot-password)
            }
        }
    }

    //////////////////FORGOT API USER/////////////////////////

    // Forgot Password API (API-users/forgot-password)
    private fun callForgotAPIUser(email: String)
    {
        if (CommonClass(this, this).checkInternetConn(this))
            forgotPasswordHandler.getPassword(email)

        else
            CommonClass(this, this).internetIssue(this)
    }

    // Forgot Password API Response (API-users/forgot-password)
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            CommonClass(this@ForgotPasswordActivity, this@ForgotPasswordActivity)
                .showToast(mainPojo.message,rlForgot)

            finish()
        }
        else
            CommonClass(this@ForgotPasswordActivity, this@ForgotPasswordActivity)
                .showToast(mainPojo.message,rlForgot)
    }

    override fun showLoader()
    {
        rlLoader.visibility=View.VISIBLE
    }

    override fun hideLoader()
    {
        rlLoader.visibility=View.GONE
    }

    private fun getEnv(): MyApplication
    {
        return application as MyApplication
    }
}