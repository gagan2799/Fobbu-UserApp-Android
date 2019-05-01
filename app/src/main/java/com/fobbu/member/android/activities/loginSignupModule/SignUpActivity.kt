package com.fobbu.member.android.activities.loginSignupModule

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginSignupModule.presenter.SignUpActivityHandler
import com.fobbu.member.android.activities.loginSignupModule.presenter.SignUpActivityPresenter
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(),ActivityView
{
    private lateinit var dataAdaperSelectService: ArrayAdapter<String>

    private lateinit var webServiceApi: WebServiceApi

    lateinit var signUpactivityHandler: SignUpActivityHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        initialise()            // Method for initializing all the variables that are used in the class

        addClicks()          // Functionality of  all clicks present in the activity are handled here

        fetchDeviceToken()       // Method for fetching device token
    }

    // Method for initializing all the variables that are used in the class
    private fun initialise()
    {
        signUpactivityHandler= SignUpActivityPresenter(this, this)

        webServiceApi = getEnv().getRetrofit()

        val itemSelectGender = arrayOf(this.resources.getString(R.string.selectGender),this.resources.getString(R.string.male),
            this.resources.getString(R.string.female),this.resources.getString(R.string.not_specified))

        dataAdaperSelectService = ArrayAdapter(this, R.layout.spinnertype, itemSelectGender)

        spinnerSelectGender.adapter = dataAdaperSelectService as SpinnerAdapter?
    }

    // Functionality of  all clicks present in the activity are handled here
    private fun addClicks()
    {
        spinnerSelectGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
            {
                tvGender.text = p0!!.selectedItem.toString()
            }
        }

        tvGender.setOnClickListener {
            spinnerSelectGender.performClick()
        }

        tvAlreadyRegistered.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

            finish()
        }

        tvStartFobbu.setOnClickListener {
            when
            {
                etFullName.text.trim().toString() =="" ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_full_name),rlSignUp)

                    etFullName.requestFocus()
                }

                etMobile.text.trim().toString() =="" -> {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_mobile),rlSignUp)
                    etMobile.requestFocus()
                }

                etMobile.text.trim().toString().length<10->
                {
                    CommonClass(this,this).showToast(resources.getString(R.string.correct_mobile_number_msg),rlSignUp)

                    etFullName.requestFocus()
                }

                etEmail.text.trim().toString() =="" ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_email),rlSignUp)

                    etEmail.requestFocus()
                }

                !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.trim().toString()).matches() ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.correct_email_error),rlSignUp)

                    etEmail.requestFocus()
                }

                etPassword.text.trim().toString() =="" ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_password),rlSignUp)

                    etPassword.requestFocus()
                }

                etPassword.text.trim().length < 6 ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_valid_password),rlSignUp)

                    etPassword.requestFocus()
                }

                tvGender.text.trim().toString() ==resources.getString(R.string.selectGender) ->
                {
                    CommonClass(this, this).showToast(resources.getString(R.string.please_enter_gender),rlSignUp)

                    etPassword.requestFocus()
                }
                else ->
                {
                    val gender: String = if (tvGender.text.trim().toString()==resources.getString(R.string.not_specified))
                        resources.getString(R.string.other)

                     else
                        tvGender.text.trim().toString()

                    callSignUpAPIUser("user", etFullName.text.trim().toString(), etEmail.text.trim().toString(), etPassword.text.trim().toString(), etMobile.text.trim().toString(), gender.trim().toLowerCase())
                }
            }
        }
    }

    // Method for fetching device token
    private fun fetchDeviceToken()
    {
        val api = GoogleApiAvailability.getInstance()

        val code = api.isGooglePlayServicesAvailable(this@SignUpActivity)

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
            System.out.println("ERRROR")
    }

    //////////////////SIGN UP API USER/////////////////////////

    // Signup API (API-users/signup)
    private fun callSignUpAPIUser(user_type: String,display_name:String, email:String,password:String,mobile_number:String,gender:String )
    {
        if (CommonClass(this, this).checkInternetConn(this))
        {
            val token = CommonClass(this@SignUpActivity, this@SignUpActivity).getString("device_token")

            val firstName :String

            var lastName=""

            var name=""

            name= if (display_name.startsWith(""))
                display_name.trim()

            else
                display_name

            if(name.contains("\\s+".toRegex()))
            {
                firstName = name.split("\\s+".toRegex())[0]

                lastName = name.split("\\s+".toRegex())[1]
            }
            else
                firstName = name

           // rlLoader.visibility = View.VISIBLE
            signUpactivityHandler.sendSignUpData(user_type,firstName, lastName,name,email,password,mobile_number,gender,token)
        }
        else
            CommonClass(this, this).internetIssue(this)
    }

    // Signup API Response (API-users/signup)
    override fun onRequestSuccessReport(mainPojo: MainPojo)
    {
        // rlLoader.visibility = View.GONE
        if (mainPojo.success== "true")
        {
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

            CommonClass(this@SignUpActivity, this@SignUpActivity)
                .putString("user_url", mainPojo.urls.user)

            CommonClass(this@SignUpActivity, this@SignUpActivity)
                .putString("x_access_token", mainPojo.token)

            CommonClass(this@SignUpActivity, this@SignUpActivity)
                .putString("member_id", mainPojo.getData().member_id)

            startActivity(Intent(this@SignUpActivity,
                SMSVerificationActivity::class.java))

            finish()
        }
        else
            CommonClass(this@SignUpActivity, this@SignUpActivity)
                .showToast(mainPojo.message,rlSignUp)
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