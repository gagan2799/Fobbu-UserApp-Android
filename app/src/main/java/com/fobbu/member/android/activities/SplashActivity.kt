package com.fobbu.member.android.activities

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.activities.loginSignupModule.SMSVerificationActivity
import com.fobbu.member.android.backgroundServices.FetchStatusAPI
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.tutorial.TutorialActivity
import com.fobbu.member.android.utils.CommonClass
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {


    lateinit var animationRight: Animation
    lateinit var animationLeft: Animation
    lateinit var animationFade: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashAnimation()

        fetchDeviceToken()

        try {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nm.cancelAll()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (CommonClass(this, this).getString(RsaConstants.ServiceSaved.fobbuRequestId) != "") {
            stopService(Intent(this, FetchStatusAPI::class.java))

            startService(Intent(this, FetchStatusAPI::class.java))
        }

    }

    // Method for launching different screens
    private fun navigateToScreen() {

        when {
            CommonClass(this, this).getString("CoachMark_first_time") == "" ->
            {
                startActivity(Intent(this, TutorialActivity::class.java))

                finish()
            }


            CommonClass(this, this).getString("isSmsVerified") == "false" ->
            {
                startActivity(Intent(this, SMSVerificationActivity::class.java))

                finish()
            }

            CommonClass(this, this).getString("isSmsVerified") == "true" ->
            {
                if (CommonClass(this, this).getString("mobile_number") != "")
                {
                    startActivity(Intent(this, DashboardActivity::class.java))

                    finish()
                }
            }

            else ->
            {
                startActivity(Intent(this, TutorialActivity::class.java))

                finish()
            }
        }
    }


    override fun onStop() {
        super.onStop()
        try {
            linearLayoutImageLeft.clearAnimation()
            animationRight.setAnimationListener(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Method to manage all the animations of the activity
    private fun splashAnimation() {

        // slide left animation
        animationLeft = AnimationUtils.loadAnimation(
            this
            , R.anim.slide_left
        )
        linearLayoutImageLeft.clearAnimation()

        //slide right animation
        animationRight = AnimationUtils.loadAnimation(this, R.anim.slide_right)
        linearLayoutImageRight.clearAnimation()

        // fade animation
        animationFade = AnimationUtils.loadAnimation(applicationContext, R.anim.fade)
        ivCenter.clearAnimation()
        ivCenter.animation = animationFade
        ivFobbuText.startAnimation(animationFade)

        // animation listener on fade animation
        animationFade.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(p0: Animation?) {

                linearLayoutImageLeft.startAnimation(animationLeft)
                linearLayoutImageRight.startAnimation(animationRight)
                linearLayoutCarScooterSplash.visibility = View.VISIBLE
                imageViewLeftSplash.visibility = View.VISIBLE

                // animation listener on slide right animation
                animationRight.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        linearLayoutBottomTextSplash.visibility = View.VISIBLE

                        // slide from bottom to up animation
                        val animationBottomToTop: Animation =
                            AnimationUtils.loadAnimation(applicationContext, R.anim.bottoms_up)
                        linearLayoutBottomTextSplash.startAnimation(animationBottomToTop)

                        // handler for shooting next activity after certain time period
                        Handler().postDelayed({
                            //HANDLE WHERE SHOULD APP LAND IN
                            navigateToScreen()

                        }, 2000)
                    }

                    override fun onAnimationStart(p0: Animation?) {
                    }

                })
            }

            override fun onAnimationStart(p0: Animation?) {
                ivCenter.startAnimation(animationFade)

            }

        })

    }

    // Method for fetching device token
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
