package com.fobbu.member.android.activities.waitingScreenModule

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.paymentModule.WorkSummaryActivity
import com.fobbu.member.android.interfaces.ChangeRSAFragments
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_waiting_screen_white.*

class WaitingScreenWhite : AppCompatActivity() {

    private var strWhich = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_waiting_screen_white)

        handleClicks()
    }

    // Click functionality of this activity's clicks are handled here
    private fun handleClicks() {

        strWhich = intent.getStringExtra("from_where")

        switchLayouts(strWhich)

        rlNewVehicleAdded.setOnClickListener {

            strWhich = "building_live"

            switchLayouts(strWhich)
        }

        rlBuildingLiveTrack.setOnClickListener {

            CommonClass(this, this).putString("OnGoingRSA_Screen", "YES")

            CommonClass(this, this).putString("OnGoingRSA_Screen_Type", resources.getString(R.string.rsa_live))

            finish()
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
            startActivity(Intent(this,WorkSummaryActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    // method for switching layouts
    private fun switchLayouts(strWhich: String?) {

        when (strWhich) {
            "building_live" -> {
                rlBuildingLiveTrack.visibility = View.VISIBLE
                rlCodeValidated.visibility = View.GONE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlVehicleAccessed.visibility = View.GONE
            }
            "code_valid" -> {
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.VISIBLE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlVehicleAccessed.visibility = View.GONE
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
            }
            "building_work_summary" -> {
                rlVehicleAccessed.visibility = View.GONE
                rlAccessingVehicle.visibility = View.GONE
                rlBuildingLiveTrack.visibility = View.GONE
                rlCodeValidated.visibility = View.GONE
                rlNewVehicleAdded.visibility = View.GONE
                rlBuildingWorkSummary.visibility = View.VISIBLE
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

}