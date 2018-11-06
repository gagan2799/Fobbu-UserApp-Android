package com.fobbu.member.android.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.apiInterface.MyApplication
import com.fobbu.member.android.apiInterface.WebServiceApi
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import kotlinx.android.synthetic.main.activity_vehicle_list.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class VehicleListActivity : AppCompatActivity() {

    private lateinit var webServiceApi: WebServiceApi

    private var imageFrom = ""
    private var vehicleType = "2wheeler"

    private var dataListMain: ArrayList<HashMap<String, Any>> = ArrayList()
    private var dataListTwo: ArrayList<HashMap<String, Any>> = ArrayList()
    private var dataListFour: ArrayList<HashMap<String, Any>> = ArrayList()

    var fromWhere = ""

    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        initialise()
        addClicks()
        vehicleListApi()
    }

    private fun initialise() {

        webServiceApi = getEnv().getRetrofit()

        if (intent.hasExtra("from_where")) {
            fromWhere = "RSA"

        }
        recyclerViewVehicles.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vehicleAdapter = VehicleAdapter()
        recyclerViewVehicles.adapter = vehicleAdapter
        vehicleAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (fromWhere == "RSA")
            startActivity(Intent(this, WaitingScreenWhite::class.java).putExtra("from_where", "building_live"))

        finish()
    }

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
    private fun vehicleListApi() {

        rlLoader.visibility = View.VISIBLE

        if (CommonClass(this, this).checkInternetConn(this)) {

            val tokenHeader = CommonClass(this, this).getString("x_access_token")

            val userId = CommonClass(this, this).getString("_id")

            val searchServicesApi = webServiceApi.fetchUserVehicles(tokenHeader, userId)

            searchServicesApi.enqueue(object : retrofit2.Callback<MainPojo> {
                override fun onResponse(call: Call<MainPojo>?, response: Response<MainPojo>?) {
                    rlLoader.visibility = View.GONE

                    println(response.toString())

                    val mainPojo = response!!.body()

                    if (mainPojo!!.success == "true") {
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
                    } else {

                    }
                }

                override fun onFailure(call: Call<MainPojo>?, t: Throwable?) {

                    rlLoader.visibility = View.GONE
                    t!!.printStackTrace()
                }
            })
        }
    }

    inner class VehicleAdapter : RecyclerView.Adapter<VehicleAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
            val view = LayoutInflater.from(this@VehicleListActivity).inflate(
                R.layout.inflate_vehicle_adapter
                , p0, false
            )

            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.tvVehicleName.text = dataListMain[position]["vehicle_brand"].toString()
            holder.tvVehicleNumber.text = dataListMain[position]["vehicle_registration_number"].toString()
            holder.tvYear.text = dataListMain[position]["make_of_year"].toString()

        }

        override fun getItemCount(): Int {
            return (dataListMain.size)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var ivImage = view.findViewById(R.id.ivImage) as ImageView
            var tvVehicleName = view.findViewById(R.id.tvVehicleName) as TextView
            var tvYear = view.findViewById(R.id.tvYear) as TextView
            var tvVehicleNumber = view.findViewById(R.id.tvVehicleNumber) as TextView
        }
    }


    private fun getEnv(): MyApplication {
        return application as MyApplication
    }
}