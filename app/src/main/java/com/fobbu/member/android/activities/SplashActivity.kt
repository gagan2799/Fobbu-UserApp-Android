package com.fobbu.member.android.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.loginActivity.LoginActivity
import com.fobbu.member.android.tutorial.TutorialActivity
import com.fobbu.member.android.utils.CommonClass
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fade()

        fetchDeviceToken()

        Handler().postDelayed({
            //HANDLE WHERE SHOULD APP LAND IN
            navigateToScreen()
        }, 3000)
    }

    private fun navigateToScreen() {

        when {
            CommonClass(this,this).getString("CoachMark_first_time")=="" -> {
                startActivity(Intent(this,TutorialActivity::class.java))
                finish()
            }
            CommonClass(this,this).getString("mobile_number")!="" -> {
                startActivity(Intent(this,DashboardActivity::class.java))
                finish()
            }
            else -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun fade() {
        val animation1 = AnimationUtils.loadAnimation(applicationContext, R.anim.fade)
        ivCenter.startAnimation(animation1)
        ivFobbuText.startAnimation(animation1)
    }

    private fun fetchDeviceToken() {
        val api = GoogleApiAvailability.getInstance()

        val code = api.isGooglePlayServicesAvailable(this@SplashActivity)

        if (code == ConnectionResult.SUCCESS) {
            // Do Your Stuff Here
            if (FirebaseInstanceId.getInstance().token != null) {
                val refreshedToken = FirebaseInstanceId.getInstance().token

                System.out.println("RE 0 $refreshedToken")

                System.out.println("SUCCESSS")

                getSharedPreferences("Fobbu_Member_Prefs", MODE_PRIVATE).edit()
                    .putString("device_token", refreshedToken).apply()
            }

        } else {
            System.out.println("ERRROR")
        }
    }
}
