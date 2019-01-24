package com.fobbu.member.android.activities.vehicleModule

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.vehicleModule.adapter.VehicleAdapter
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditActivityHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.AddEditVehiclePresenter
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListPresenter
import com.fobbu.member.android.activities.vehicleModule.view.AddEditVehicleAcivityView
import com.fobbu.member.android.activities.waitingScreenModule.WaitingScreenWhite
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.interfaces.DeleteVehicleClickListener
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import java.util.*
import kotlin.collections.HashMap

class VehicleListActivity : AppCompatActivity(),ActivityView,DeleteVehicleClickListener,AddEditVehicleAcivityView{



    private lateinit var webServiceApi: WebServiceApi

    private var imageFrom = ""
    private var vehicleType = "2wheeler"
    private lateinit var vehicleHandler: VehicleListHandler
    private lateinit var addEditHandler: AddEditActivityHandler

    private var dataListMain: ArrayList<HashMap<String, Any>> = ArrayList()
    private var dataListTwo: ArrayList<HashMap<String, Any>> = ArrayList()
    private var dataListFour: ArrayList<HashMap<String, Any>> = ArrayList()

    var fromWhere = ""

    var vehicleID=""

    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        initialise()
        addClicks()
    }

    override fun onResume() {
        super.onResume()

        vehicleListApi()
    }

    //// All initialise in this method for this class
    private fun initialise()
    {
        vehicleHandler= VehicleListPresenter(this, this)

        addEditHandler= AddEditVehiclePresenter(this, this)

        webServiceApi = getEnv().getRetrofit()

        if (intent.hasExtra("from_where"))
        {
            fromWhere = "RSA"
        }

        /// Vehicle Adapter handled here
        recyclerViewVehicles.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vehicleAdapter = VehicleAdapter(this, dataListMain)
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

        tvAddEditVehicle.setOnClickListener { finish() }

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

    override fun onViewClick(vehicleID: String) {
        deleteVehicle(vehicleID)
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

            if (vehicleType=="4wheeler")
            {
                tvScooter.setImageResource(R.drawable.scooter_gray)
                view_scooter.visibility = View.GONE

                tvCar.setImageResource(R.drawable.car_red)
                view_car.visibility = View.VISIBLE

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
                    tvNodata.text="No 2 Wheeler Added"
                    recyclerViewVehicles.visibility=View.GONE
                }



            }
            else
            {
                tvScooter.setImageResource(R.drawable.scooter_red)
                view_scooter.visibility = View.VISIBLE

                tvCar.setImageResource(R.drawable.car_gray)
                view_car.visibility = View.GONE

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

    ///DELETE VEHICLE  POPUP
    private fun deleteVehicle(vehicleID: String) {

        this.vehicleID =vehicleID
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(resources.getString(R.string.Delete))
        alertDialog.setMessage(resources.getString(R.string.are_you_sure_you_want_to_delete))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK") { _, _ ->
            alertDialog.dismiss()
            if (CommonClass(this,this).checkInternetConn(this))
            {
                val tokenHeader = CommonClass(this, this).getString("x_access_token")

                val userId = CommonClass(this, this).getString("_id")

                addEditHandler.deleteVehicle(tokenHeader,vehicleID,userId)


            }

        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CANCEL") { _, _ ->
            alertDialog.dismiss()
        }
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        alertDialog.show()
    }


    override fun onRequestSuccessReportEdit(mainPojo: MainPojo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestSuccessUpdateVehicle(mainPojo: MainPojo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeleteVehicleSuccessUpdateVehicle(mainPojo: MainPojo) {

        if (mainPojo.success=="true")
        {
            for (i in dataListMain.size-1 downTo 0)
            {
                if (vehicleID==dataListMain[i]["_id"])
                {
                    dataListMain.removeAt(i)
                    break
                }
            }

            for (i in dataListTwo.size-1 downTo 0)
            {
                if (vehicleID==dataListTwo[i]["_id"])
                {
                    dataListTwo.removeAt(i)
                    break

                }
            }

            for (i in dataListFour.size-1 downTo 0)
            {
                if (vehicleID==dataListFour[i]["_id"])
                {
                    dataListFour.removeAt(i)
                    break
                }
            }
            vehicleAdapter.notifyDataSetChanged()
            println("data list$dataListMain")
        }else
        {
            Toast.makeText(this,mainPojo.message, Toast.LENGTH_SHORT).show()
        }
    }

}