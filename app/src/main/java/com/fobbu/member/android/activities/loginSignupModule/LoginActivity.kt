package com.fobbu.member.android.activities.loginSignupModule

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginSignupModule.presenter.LoginActivityHandler
import com.fobbu.member.android.activities.loginSignupModule.presenter.LoginActivityPresenter
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), ActivityView
{
    private lateinit var webServiceApi: WebServiceApi

    lateinit var loginActivityHandler: LoginActivityHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        initView()               // function for initialising all the variables and views of the class

        addClicks()              // Functionality of  all clicks present in the activity are handled here

        fetchDeviceToken()          // Method for fetching device token
    }

    // function for initialising all the variables and views of the class
    private fun initView()
    {
        loginActivityHandler = LoginActivityPresenter(this, this)
    }


    // Method for fetching device token
    private fun fetchDeviceToken()
    {
        val api = GoogleApiAvailability.getInstance()

        val code = api.isGooglePlayServicesAvailable(this@LoginActivity)

        if (code == ConnectionResult.SUCCESS)
        {
            // Do Your Stuff Here
            if (FirebaseInstanceId.getInstance().token != null)
            {
                val refreshedToken = FirebaseInstanceId.getInstance().token

                System.out.println("RE 0 $refreshedToken")

                System.out.println("SUCCESSS")

                getSharedPreferences("Fobbu_Member_Prefs", MODE_PRIVATE).edit()
                    .putString("device_token", refreshedToken).apply()
            }
        }
        else
        {
            System.out.println("ERRROR")
        }
    }

    // Functionality of  all clicks present in the activity are handled here
    private fun addClicks()
    {
        webServiceApi = getEnv().getRetrofit()

        ivBack.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))

            finish()
        }

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        tvStartFobbu.setOnClickListener {
            when
            {
                etMobile.text.trim().isEmpty() ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_mobile))

                    etMobile.requestFocus()
                }

                etMobile.text.trim().length<10->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.correct_mobile_number_msg))

                    etMobile.requestFocus()
                }

                etPassword.text.trim().isEmpty() ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_password))

                    etPassword.requestFocus()
                }

                etPassword.text.trim().length < 6 ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_valid_password))

                    etPassword.requestFocus()
                }

                else ->
                {
                    callLoginAPIUser(etMobile.text.trim().toString(), etPassword.text.trim().toString())
                }
            }
        }
    }

    override fun onBackPressed()
    {
        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))

        finish()
    }

    //////////////////LOGIN API USER/////////////////////////

    // Login API (API-users/login)
    private fun callLoginAPIUser(mobile: String, password: String)
    {
        if (CommonClass(this, this).checkInternetConn(this)) {

            val token = CommonClass(this@LoginActivity, this@LoginActivity).getString("device_token")

            // rlLoader.visibility = View.VISIBLE

            loginActivityHandler.sendLoginData(mobile, password, token)

        } else {

            CommonClass(this, this).internetIssue(this)
        }
    }

    // Login API Response (API-users/login)
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        if (mainPojo.success == "true")
        {
            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("_id", mainPojo!!.getData()._id)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("display_name", mainPojo.getData().display_name)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("email", mainPojo.getData().email)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("gender", mainPojo.getData().gender)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("mobile_number", mainPojo.getData().mobile_number)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("first_name", mainPojo.getData().first_name)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("last_name", mainPojo.getData().last_name)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("pin", mainPojo.pin)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("x_access_token", mainPojo.token)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("user_image", mainPojo.getData().profile)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("user_url", mainPojo.urls.user)

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("user_password", etPassword.text.toString())

            CommonClass(this@LoginActivity, this@LoginActivity)
                .putString("member_id", mainPojo.getData().member_id)

            if (mainPojo.active_requests.size > 0)
            {
                val list = mainPojo.active_requests

                CommonClass(this, this).putString(RsaConstants.ServiceSaved.serviceNameSelected, list[0]["static_name"].toString())

                CommonClass(this@LoginActivity, this@LoginActivity)
                    .putString(RsaConstants.ServiceSaved.fobbuRequestId, list[0]["_id"].toString())

                CommonClass(this@LoginActivity, this@LoginActivity)
                    .putString(RsaConstants.RsaTypes.checkIfOnGoingRsaRequest, "YES")

                CommonClass(this@LoginActivity, this@LoginActivity)
                    .putString(RsaConstants.RsaTypes.checkStatus, list[0]["status"].toString())

                if (list[0].containsKey("otp"))
                {
                    val  otp = if (list[0]["otp"].toString().contains("\\.".toRegex()))
                        list[0]["otp"].toString().split("\\.".toRegex())[0]

                    else
                        list[0]["otp"].toString()

                    CommonClass(this, this).putString(RsaConstants.ServiceSaved.otpStart,otp)
                }
            }
            startActivity(Intent(this@LoginActivity, GeneratePINActivity::class.java))

            finish()
        }
        else
        {
            CommonClass(this@LoginActivity, this@LoginActivity)
                .showToast(mainPojo.message)
        }

    }

    override fun showLoader()
    {
        rlLoader.visibility = View.VISIBLE
    }

    override fun hideLoader()
    {
        rlLoader.visibility = View.GONE
    }

    private fun getEnv(): MyApplication
    {
        return application as MyApplication
    }
}