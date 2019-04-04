package com.fobbu.member.android.activities.selectVehicleActivity.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R

class Vehicle2WheelerAdapter(var activity: Activity,var dataList:ArrayList<HashMap<String,Any>>):RecyclerView.Adapter<Vehicle2WheelerAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder
    {
        val view = LayoutInflater.from(activity).inflate(R.layout.inflate_vehicle_adapter, p0, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return dataList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int)
    {
        p0.ivDELETE.visibility=View.GONE

        p0.ivImage.visibility=View.GONE

        p0.tvVehicleName.text = dataList[p1]["vehicle_brand"].toString()

        p0.tvVehicleNumber.text = dataList[p1]["vehicle_registration_number"].toString()

        p0.tvYear.text = dataList[p1]["make_of_year"].toString()

        p0.tvSubModel?.text=dataList[p1]["vehicle_sub_model"].toString()

    }

    // inner view holder class
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var ivImage = view.findViewById(R.id.ivImage) as ImageView

        var tvVehicleName = view.findViewById(R.id.tvVehicleName) as TextView

        var tvSubModel = view.findViewById(R.id.tvSubModelAdapter) as TextView?

        var tvYear = view.findViewById(R.id.tvYear) as TextView

        var tvVehicleNumber = view.findViewById(R.id.tvVehicleNumber) as TextView

        var ivDELETE = view.findViewById(R.id.ivDelete) as ImageView
    }
}