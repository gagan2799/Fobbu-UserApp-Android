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
import com.fobbu.member.android.R
import com.fobbu.member.android.activities.vehicleModule.AddEditVehicleActivity
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListHandler
import com.fobbu.member.android.activities.vehicleModule.presenter.VehicleListPresenter
import com.fobbu.member.android.modals.MainPojo
import com.fobbu.member.android.utils.CommonClass
import com.fobbu.member.android.view.ActivityView
import com.squareup.picasso.Picasso

class VehicleAdapter(internal var activity: Activity, internal  var dataListMain:ArrayList<HashMap<String,Any>>):

    RecyclerView.Adapter<VehicleAdapter.MyViewHolder>(),ActivityView {


    lateinit var vehicleListHandlerFactory  :VehicleListHandler
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

        vehicleListHandlerFactory=VehicleListPresenter(activity,this)
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

        }

    }


    ///LOGOUT POPUP
    private fun showLogoutPopup() {

        val alertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setTitle(activity.resources.getString(R.string.Delete))
        alertDialog.setMessage(activity.resources.getString(R.string.are_you_sure_you_want_to_delete))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK") { _, _ ->
            alertDialog.dismiss()
            if (CommonClass(activity,activity).checkInternetConn(activity))
            {
                val tokenHeader = CommonClass(activity, activity).getString("x_access_token")

                val userId = CommonClass(activity, activity).getString("_id")

                /*vehicleListHandlerFactory.deleteVehicle(tokenHeader,)*/
            }


            //logoutApi()
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
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivImage = view.findViewById(R.id.ivImage) as ImageView
        var tvVehicleName = view.findViewById(R.id.tvVehicleName) as TextView
        var tvYear = view.findViewById(R.id.tvYear) as TextView
        var tvVehicleNumber = view.findViewById(R.id.tvVehicleNumber) as TextView
        var ivDELETE = view.findViewById(R.id.ivDelete) as ImageView
    }

    override fun onRequestSuccessReport(mainPojo: MainPojo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}