package com.fobbu.member.android.activities.waitingScreenModule

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.dashboardActivity.DashboardActivity
import com.fobbu.member.android.activities.paymentModule.GetSetGoActivity
import com.fobbu.member.android.activities.paymentModule.OdsGetSetGoActivity
import com.fobbu.member.android.activities.paymentModule.WorkSummaryActivity
import com.fobbu.member.android.fcm.FcmPushTypes
import com.fobbu.member.android.fragments.HomeFragment
import com.fobbu.member.android.fragments.rsaFragmentModule.RsaConstants
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_waiting_screen_white.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import java.lang.Exception

class WaitingScreenWhite : AppCompatActivity() {

    private var strWhich = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_waiting_screen_white)

        handleClicks()

        try {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            nm.cancelAll()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Click functionality of this activity's clicks are handled here
    private fun handleClicks() {

        strWhich = intent.getStringExtra("from_where")

        switchLayouts(strWhich)

       /* rlNewVehicleAdded.setOnClickListener {

            strWhich = "building_live"

            switchLayouts(strWhich)
        }

        rlBuildingLiveTrack.setOnClickListener {

            goToRsaLiveScreen()
        }

        rlCodeValidated.setOnClickListener {
            switchLayouts("wallet_accessing")
        }

        rlAccessingVehicle.setOnClickListener {
            switchLayouts("vehicle_accessed")
        }

        rlVehicleAccessed.setOnClickListener {
            switchLayouts("building_work_summary")
        }

        rlBuildingWorkSummary.setOnClickListener {
            startActivity(
                Intent(this, WorkSummaryActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }*/
    }

    ///GOING TO RSA LIVE SCREEN
    private fun goToRsaLiveScreen() {

        startActivity(Intent(this, DashboardActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreen, "YES")

        CommonClass(this, this).putString(RsaConstants.RsaTypes.onGoingRsaScreenType, resources.getString(R.string.rsa_live))

        finish()
    }

    // method for switching layouts
    private fun switchLayouts(strWhich: String?) {

        when (strWhich) {

            "profile"->
            {
                rlNewVehicleAdded.visibility=View.VISIBLE

                tvVehicleAdd.text=getString(R.string.thank_you)

                tvWhiteSubMessage.text="you have successfully completed \nyour profile."

                Handler().postDelayed({
                    startActivity(Intent(this, DashboardActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                },2000)

            }

            "building_live" -> {
                rlBuildingLiveTrack.visibility = View.VISIBLE
                rlCodeValidated.visibility = View.GONE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlVehicleAccessed.visibility = View.GONE

                Handler().postDelayed({
                    goToRsaLiveScreen()
                }, 1000)
            }
            "code_valid" -> {
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.VISIBLE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlVehicleAccessed.visibility = View.GONE

                if(CommonClass(this,this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                    RsaConstants.ServiceName.flatTyre ||
                    CommonClass(this,this).getString(RsaConstants.ServiceSaved.serviceNameSelected) ==
                    RsaConstants.ServiceName.burstTyre)
                {
                    Handler().postDelayed({
                        switchLayouts("wallet_accessing")
                    }, 1200)
                }
                else
                {
                    Handler().postDelayed({
                        startActivity(Intent(this, GetSetGoActivity::class.java)
                            .setFlags
                                (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))

                        finish()
                    }, 1200)
                }
            }

            "code_valid_ods" -> {
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.VISIBLE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlVehicleAccessed.visibility = View.GONE
                tvCodeValidatedSubMessage.visibility = View.GONE

                Handler().postDelayed({
                        startActivity(Intent(this, OdsGetSetGoActivity::class.java)
                            .setFlags
                                (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(RsaConstants.Ods.static_name,"washing"))

                        finish()
                    }, 1200)

            }

            "wallet_accessing" -> {
                rlAccessingVehicle.visibility = View.VISIBLE
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlVehicleAccessed.visibility = View.GONE
            }
            "vehicle_accessed" -> {
                rlVehicleAccessed.visibility = View.VISIBLE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE

                Handler().postDelayed({
                    switchLayouts("building_work_summary")
                }, 1000)
            }
            "building_work_summary" -> {
                rlVehicleAccessed.visibility = View.GONE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.VISIBLE

                Handler().postDelayed({
                    startActivity(
                        Intent(this, WorkSummaryActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    finish()
                }, 1000)
            }
            "new_vehicle_added" -> {
                rlNewVehicleAdded.visibility = View.VISIBLE
                rlVehicleAccessed.visibility = View.GONE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()

        if(strWhich =="wallet_accessing" || strWhich =="code_valid")
        {
            val intent =   Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter(FcmPushTypes.Types.moneyRequestedBroadcast)
        registerReceiver(changeRSALiveScreenReceiver, filter)
    }

    private val changeRSALiveScreenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            println("ON RECEIVE BROADCAST WHITE SCREEN " + intent.getStringExtra("navigate_to"))

            when {
                intent.getStringExtra("navigate_to") == FcmPushTypes.Types.moneyRequested -> {
                    switchLayouts("vehicle_accessed")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(changeRSALiveScreenReceiver)
        } catch (e: Exception) {

        }
    }

}