package com.fobbu.member.android.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.fobbu.member.android.R
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var dataAdaperSelectService: ArrayAdapter<String>
    private lateinit var webServiceApi: WebServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initialise()
        addClicks()
    }

    private fun initialise() {
        webServiceApi = getEnv().getRetrofit()
        val itemSelectGender = arrayOf(this.resources.getString(R.string.male),
                this.resources.getString(R.string.female), this.resources.getString(R.string.other))
        dataAdaperSelectService = ArrayAdapter(this, R.layout.spinnertype, itemSelectGender)
        spinnerSelectGender.adapter = dataAdaperSelectService
        spinnerSelectGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tvGender.text = p0!!.selectedItem.toString()
            }
        }
    }

    private fun addClicks() {

        tvGender.setOnClickListener {
            spinnerSelectGender.performClick()
        }

        tvAlreadyRegistered.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        tvStartFobbu.setOnClickListener {
            when {
                etFullName.text.toString() =="" -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_full_name))
                    etFullName.requestFocus()
                }

                etMobile.text.toString() =="" -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_mobile))
                    etMobile.requestFocus()
                }

                etEmail.text.toString() =="" -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_email))
                    etEmail.requestFocus()
                }

                !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches() -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.correct_email_error))
                    etEmail.requestFocus()
                }

                etPassword.text.toString() =="" -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_password))
                    etPassword.requestFocus()
                }
                else -> {
                    callSignUpAPIUser("user",
                        etFullName.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etMobile.text.toString(),
                        tvGender.text.toString().toLowerCase())
                }
            }
        }
    }

    //////////////////SIGN UP API USER/////////////////////////
    private fun callSignUpAPIUser(user_type: String,display_name:String,
                                  email:String,password:String,mobile_number:String,gender:String ) {


        if (CommonClass(this, this).checkInternetConn(this)) {

            val token = CommonClass(this@SignUpActivity, this@SignUpActivity).getString("device_token")


            val firstName :String
            var lastName=""

            if(display_name.contains("\\s+".toRegex()))
            {
                firstName = display_name.split("\\s+".toRegex())[0]
                lastName = display_name.split("\\s+".toRegex())[1]
            }
            else
                firstName = display_name

            rlLoader.visibility = View.VISIBLE

            val callloginApi = webServiceApi.postSignUp(user_type,firstName,
                lastName,display_name,email,password,mobile_number,gender)

            callloginApi.enqueue(object : Callback<MainPojo> {
                override fun onResponse(call: Call<MainPojo>, response: Response<MainPojo>) {

                    try {

                        val mainPojo = response.body()

                        if (mainPojo!!.success == "true") {

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("_id",mainPojo.getData()._id)

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("display_name",mainPojo.getData().display_name)

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("email",mainPojo.getData().email)

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("gender",mainPojo.getData().gender)

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("mobile_number",mainPojo.getData().mobile_number)

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("first_name",mainPojo.getData().first_name)

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("last_name",mainPojo.getData().last_name)

                            CommonClass(this@SignUpActivity,this@SignUpActivity)
                                .putString("pin",mainPojo.pin)

                            startActivity(Intent(this@SignUpActivity,SMSVerificationActivity::class.java))

                            finish()

                        } else {
                            CommonClass(this@SignUpActivity, this@SignUpActivity)
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