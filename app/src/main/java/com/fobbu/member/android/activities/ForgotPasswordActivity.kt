package com.fobbu.member.android.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_forgot.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var webServiceApi: WebServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        addClicks()
    }

    private fun addClicks() {

        webServiceApi = getEnv().getRetrofit()

        ivBack.setOnClickListener { finish() }

        tvReset.setOnClickListener {
            when {
                etEmail.text.isEmpty() -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_email))
                }
                !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim()).matches() -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.correct_email_error))
                }
                else -> {
                   callForgotAPIUser(etEmail.text.toString().trim())
                }
            }
        }
    }

    //////////////////FORGOT API USER/////////////////////////
    private fun callForgotAPIUser(email: String) {

        if (CommonClass(this, this).checkInternetConn(this)) {

            rlLoader.visibility = View.VISIBLE

            val callforgotApi = webServiceApi.forgotPassword(email)

            callforgotApi.enqueue(object : Callback<MainPojo> {
                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {

                    try {
                        val mainPojo = response.body()

                        if (mainPojo!!.success == "true") {

                            CommonClass(this@ForgotPasswordActivity, this@ForgotPasswordActivity)
                                .showToast(mainPojo.message)
                            finish()

                        } else {
                            CommonClass(this@ForgotPasswordActivity, this@ForgotPasswordActivity)
                                .showToast(mainPojo.message)
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