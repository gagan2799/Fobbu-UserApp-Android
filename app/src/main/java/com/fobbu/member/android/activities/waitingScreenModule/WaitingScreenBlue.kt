package com.fobbu.member.android.activities.waitingScreenModule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.activities.paymentModule.WorkSummaryActivity
import com.fobbu.member.android.backgroundServices.FetchStatusAPI
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_waiting_screen_blue.*
import java.lang.Exception

class WaitingScreenBlue : AppCompatActivity() {
    private var strWhich = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_waiting_screen_blue)
        initView()
        //handleclicks()
    }

    private fun initView() {

        strWhich = intent.getStringExtra("navigate_to")

        if(strWhich=="0") {
            CommonClass(this, this).putString(RsaConstants.ServiceSaved.isBlueScreenON, "1")

            CommonClass(this,this).putString(RsaConstants.ServiceSaved.isNew,"1")
        }
        changeLayout()

    }


    private fun changeLayout() {

        when (strWhich) {
            "1" -> {

                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")

                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isNew,"")
                strWhich="2"
                tvTextOne.text=resources.getString(R.string.awesome)
                tvTextTwo.text=resources.getString(R.string.fobbu_found)
                tvTextThree.visibility=View.GONE
                Handler().postDelayed({ changeLayout() },1000)
            }
            "2" -> {

                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")
                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isNew,"")
                strWhich="3"
                tvTextOne.text=resources.getString(R.string.wonderful)
                tvTextTwo.text=resources.getString(R.string.fobbu_confirmed_request)
                tvTextThree.visibility=View.VISIBLE

                if(CommonClass(this,this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                    RsaConstants.ServiceName.flatTyre
                    ||
                    CommonClass(this,this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                    RsaConstants.ServiceName.burstTyre)
                {
                    tvTextThree.text=resources.getString(R.string.fobbu_gathering_tools)
                    Handler().postDelayed({ changeLayout() },1000)
                }
                else
                {
                    tvTextThree.text=resources.getString(R.string.proceed_to_payment)

                    Handler().postDelayed({
                        startActivity(Intent(this, WorkSummaryActivity::class.java) )
                        finish()
                    },1000)

                }


            }
            "3" -> {

                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")
                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isNew,"")
                strWhich="3"

                startActivity(Intent(this, WaitingScreenWhite::class.java)
                    .putExtra("from_where", "building_live"))
                finish()
            }

            "4" -> {

                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")
                CommonClass(this,this).putString(RsaConstants.ServiceSaved.isNew,"")
                startActivity(Intent(this, DashboardActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

                CommonClass(this,this).workDoneReviewSend()
                finish()
            }
        }
    }

    // Click functionality of this activity's clicks are handled here
    private fun handleclicks() {

        rlLayout.setOnClickListener {
            when (strWhich) {
                "0" -> {

                    CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")
                    CommonClass(this,this).putString(RsaConstants.ServiceSaved.isNew,"")
                    strWhich="1"
                    tvTextOne.text=resources.getString(R.string.awesome)
                    tvTextTwo.text=resources.getString(R.string.fobbu_found)
                    tvTextThree.visibility=View.GONE
                }
                "1" -> {

                    CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")
                    CommonClass(this,this).putString(RsaConstants.ServiceSaved.isNew,"")
                    strWhich="2"
                    tvTextOne.text=resources.getString(R.string.wonderful)
                    tvTextTwo.text=resources.getString(R.string.fobbu_confirmed_request)
                    tvTextThree.text=resources.getString(R.string.fobbu_gathering_tools)
                    tvTextThree.visibility=View.VISIBLE
                }
                "2" -> {

                    CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")
                    strWhich="3"

                    startActivity(Intent(this, WaitingScreenWhite::class.java)
                        .putExtra("from_where", "building_live"))
                    finish()
                }
            }

        }
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter(FcmPushTypes.Types.acceptRequestBroadCast)
         registerReceiver(changeRSALiveScreenReceiver, filter)
    }

    private val changeRSALiveScreenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            println("Broadcast received")
            strWhich = intent.getStringExtra("navigate_to")

            if(strWhich=="4") {
                Toast.makeText(context, intent.getStringExtra("message"), Toast.LENGTH_SHORT).show()
            }
            changeLayout()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        CommonClass(this,this).putString(RsaConstants.ServiceSaved.isBlueScreenON,"")

        try {
            unregisterReceiver(changeRSALiveScreenReceiver)
        }
        catch (e:Exception)
        {

        }
    }
}