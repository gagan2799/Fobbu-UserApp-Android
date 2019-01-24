package com.fobbu.member.android.activities.vehicleModule.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.vehicleModule.AddEditVehicleActivity
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListPresenter
import com.fobbu.member.android.interfaces.DeleteVehicleClickListener
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.squareup.picasso.Picasso

class VehicleAdapter(internal var activity: Activity, private var dataListMain:ArrayList<HashMap<String,Any>>):

    RecyclerView.Adapter<VehicleAdapter.MyViewHolder>() {

    var clickListener:DeleteVehicleClickListener=activity!! as DeleteVehicleClickListener



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

        if(dataListMain[position].containsKey("images")) {
            val listImages = dataListMain[position]["images"] as ArrayList<String>

            if (listImages.size > 0)
                Picasso.get().load(listImages[0])
                    .error(R.drawable.dummy_services)
                    .into(holder.ivImage)
            else
                holder.ivImage.setImageResource(R.drawable.dummy_services)
        }
        else
            holder.ivImage.setImageResource(R.drawable.dummy_services)

        holder.itemView.setOnClickListener {

            activity.startActivity(Intent(activity,AddEditVehicleActivity::class.java).putExtra("vehicle_edit",
                dataListMain[position]))
        }


        holder.ivDELETE.setOnClickListener{
            //deleteVehicle(position)

            clickListener.onViewClick(dataListMain[position]["_id"].toString())
        }


    }




    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivImage = view.findViewById(R.id.ivImage) as ImageView
        var tvVehicleName = view.findViewById(R.id.tvVehicleName) as TextView
        var tvYear = view.findViewById(R.id.tvYear) as TextView
        var tvVehicleNumber = view.findViewById(R.id.tvVehicleNumber) as TextView
        var ivDELETE = view.findViewById(R.id.ivDelete) as ImageView

    }




}

