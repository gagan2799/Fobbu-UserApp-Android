package com.fobbu.member.android.activities.vehicleListActivity.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R

class VehicleAdapter(internal var activity: Activity, internal  var dataListMain:ArrayList<HashMap<String,Any>>):
    RecyclerView.Adapter<VehicleAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(activity).inflate(
            R.layout.inflate_vehicle_adapter
            , p0, false
        )

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return dataListMain.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvVehicleName.text = dataListMain[position]["vehicle_brand"].toString()
        holder.tvVehicleNumber.text = dataListMain[position]["vehicle_registration_number"].toString()
        holder.tvYear.text = dataListMain[position]["make_of_year"].toString()

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivImage = view.findViewById(R.id.ivImage) as ImageView
        var tvVehicleName = view.findViewById(R.id.tvVehicleName) as TextView
        var tvYear = view.findViewById(R.id.tvYear) as TextView
        var tvVehicleNumber = view.findViewById(R.id.tvVehicleNumber) as TextView
    }
}