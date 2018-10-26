package com.fobbu.member.android.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var webServiceApi: WebServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        addClicks()
    }

    private fun addClicks() {
        webServiceApi = getEnv().getRetrofit()

        ivBack.setOnClickListener {

            startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
            finish()
        }

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(
                this,
                ForgotPasswordActivity::class.java
            ))
        }

        tvStartFobbu.setOnClickListener {
            when {
                etMobile.text.isEmpty() -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_mobile))
                }
                etPassword.text.isEmpty() -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_password))
                }
                else -> {
                    callLoginAPIUser(etMobile.text.toString(),etPassword.text.toString())
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
        finish()
    }

    //////////////////LOGIN API USER/////////////////////////
    private fun callLoginAPIUser(mobile: String, password: String) {

        if (CommonClass(this, this).checkInternetConn(this)) {

            val token = CommonClass(this@LoginActivity, this@LoginActivity).getString("device_token")

            rlLoader.visibility = View.VISIBLE

            val callloginApi = webServiceApi.login(mobile,password)

            callloginApi.enqueue(object : Callback<MainPojo> {
                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {

                    try {

                        if (response.isSuccessful) {

                            val mainPojo = response.body()

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("_id",mainPojo!!.getData()._id)

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("display_name",mainPojo.getData().display_name)

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("email",mainPojo.getData().email)

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("gender",mainPojo.getData().gender)

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("mobile_number",mainPojo.getData().mobile_number)

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("first_name",mainPojo.getData().first_name)

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("last_name",mainPojo.getData().last_name)

                            CommonClass(this@LoginActivity,this@LoginActivity)
                                .putString("pin",mainPojo.pin)

                            val number = CommonClass(this@LoginActivity,this@LoginActivity)
                                .getString("Local_Number")

                            if(number == mainPojo.getData().mobile_number)
                                startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))
                            else
                            {
                                CommonClass(this@LoginActivity,this@LoginActivity)
                                    .putString("Local_Number","")
                                CommonClass(this@LoginActivity,this@LoginActivity)
                                    .putString("Local_Pin","")
                                startActivity(Intent(this@LoginActivity,GeneratePINActivity::class.java))
                            }

                            finish()

                        } else {
                            CommonClass(this@LoginActivity, this@LoginActivity)
                                .showToast("Your password not matched with your mobile number.")
                        }
                        rlLoader.visibility = View.GONE

                    } catch (e: Exception) {
                        e.printStackTrace()
                        rlLoader.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<MainPojo>, t: Throwable) {
                    rlLoader.visibility = View.GONE
                    t.printStackTrace()
                }
            })
        } else {

            CommonClass(this, this).internetIssue(this)
        }
    }


    private fun getEnv(): MyApplication {
        return application as MyApplication
    }
}