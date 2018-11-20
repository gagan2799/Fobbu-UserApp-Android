package com.fobbu.member.android.activities.vehicleListActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.vehicleListActivity.adapter.VehicleAdapter
import com.fobbu.member.android.activities.waitingScreenWhite.WaitingScreenWhite
import com.fobbu.member.android.activities.vehicleListActivity.presenter.VehicleListHandler
import com.fobbu.member.android.activities.vehicleListActivity.presenter.VehicleListPresenter
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import java.util.*
import kotlin.collections.HashMap

class VehicleListActivity : AppCompatActivity(),ActivityView{


    private lateinit var webServiceApi: WebServiceApi

    private var imageFrom = ""
    private var vehicleType = "2wheeler"
    lateinit var vehicleHandler:VehicleListHandler

    private var dataListMain: ArrayList<HashMap<String, Any>> = ArrayList()
    private var dataListTwo: ArrayList<HashMap<String, Any>> = ArrayList()
    private var dataListFour: ArrayList<HashMap<String, Any>> = ArrayList()

    var fromWhere = ""

    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        vehicleHandler= VehicleListPresenter(this,this)
        initialise()
        addClicks()
        vehicleListApi()
    }

    //// All initialise in this method for this class
    private fun initialise() {

        webServiceApi = getEnv().getRetrofit()

        if (intent.hasExtra("from_where")) {
            fromWhere = "RSA"

        }

        /// Vehicle Adapter handled here
        recyclerViewVehicles.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vehicleAdapter = VehicleAdapter(this,dataListMain)
        recyclerViewVehicles.adapter = vehicleAdapter
        vehicleAdapter.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        if (fromWhere == "RSA")
            startActivity(Intent(this, WaitingScreenWhite::class.java).putExtra("from_where", "building_live"))

        finish()
    }

    /// All click on buttons handled here
    @SuppressLint("SetTextI18n")
    private fun addClicks() {

        ivBack.setOnClickListener {
            finish()
        }

        tvScooter.setOnClickListener {

            if(vehicleType!="2wheeler")
            {
                vehicleType ="2wheeler"

                tvScooter.setImageResource(R.drawable.scooter_red)
                view_scooter.visibility = View.VISIBLE

                tvCar.setImageResource(R.drawable.car_gray)
                view_car.visibility = View.GONE

                dataListMain.clear()
                dataListMain.addAll(dataListTwo)
                vehicleAdapter.notifyDataSetChanged()

                if(dataListMain.size>0)
                {
                    tvNodata.visibility=View.GONE
                    recyclerViewVehicles.visibility=View.VISIBLE
                }
                else
                {
                    tvNodata.visibility=View.VISIBLE
                    tvNodata.text="No 2 Wheeler Added"
                    recyclerViewVehicles.visibility=View.GONE
                }

            }

        }

        tvCar.setOnClickListener {
            if(vehicleType!="4wheeler") {

                vehicleType ="4wheeler"
                tvScooter.setImageResource(R.drawable.scooter_gray)
                view_scooter.visibility = View.GONE

                tvCar.setImageResource(R.drawable.car_red)
                view_car.visibility = View.VISIBLE


                dataListMain.clear()
                dataListMain.addAll(dataListFour)
                vehicleAdapter.notifyDataSetChanged()

                if(dataListMain.size>0)
                {
                    tvNodata.visibility=View.GONE
                    recyclerViewVehicles.visibility=View.VISIBLE
                }
                else
                {
                    tvNodata.visibility=View.VISIBLE
                    tvNodata.text="No 4 Wheeler Added"
                    recyclerViewVehicles.visibility=View.GONE
                }
            }
        }

    }

    /////////////////////////////FOR API"S//////////////////////////////////////////////////

    /// Vehicle List API  (API - users/vehicles)
    private fun vehicleListApi() {

       // rlLoaderVehicleList.visibility = View.VISIBLE

        if (CommonClass(this, this).checkInternetConn(this)) {

            val tokenHeader = CommonClass(this, this).getString("x_access_token")

            val userId = CommonClass(this, this).getString("_id")

            vehicleHandler.sendVehicleData(tokenHeader,userId)

        }
    }


    /// Vehicle List Response (API -users/vehicles )
    @SuppressLint("SetTextI18n")
    override fun onRequestSuccessReport(mainPojo: MainPojo) {
       // rlLoaderVehicleList.visibility = View.GONE
        if (mainPojo.success == "true")
        {
            dataListMain.clear()
            dataListFour.clear()
            dataListTwo.clear()

            for (i in mainPojo.vehicles.indices) {
                if (mainPojo.vehicles[i]["vehicle_type"] == "4wheeler")
                    dataListFour.add(mainPojo.vehicles[i])
                else
                    dataListTwo.add(mainPojo.vehicles[i])
            }

            dataListMain.addAll(dataListTwo)
            vehicleAdapter.notifyDataSetChanged()

            if(dataListMain.size>0)
            {
                tvNodata.visibility=View.GONE
                recyclerViewVehicles.visibility=View.VISIBLE
            }
            else
            {
                tvNodata.visibility=View.VISIBLE
                tvNodata.text="No 2 Wheeler Added"
                recyclerViewVehicles.visibility=View.GONE
            }

        }else{

        }
    }

    override fun showLoader() {
        rlLoaderVehicleList.visibility=View.VISIBLE
    }

    override fun hideLoader() {
        rlLoaderVehicleList.visibility=View.GONE
    }


    private fun getEnv(): MyApplication {
        return application as MyApplication
    }
}