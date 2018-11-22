package com.fobbu.member.android.fragments.rsaFragmentModule.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R
import com.fobbu.member.android.utils.CommonClass
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.HashMap

class FobbuServiceAdapter(private var activity: Activity, private var dataListServices: ArrayList<HashMap<String, Any>> ): RecyclerView.Adapter<FobbuServiceAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(activity!!).inflate(
            R.layout.rsa_services_adapter
            , p0, false
        )

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.relativeLayout.layoutParams = CommonClass(activity!!, activity!!).giveDynamicHeightRelativeGallery()

        holder.tvText.text = dataListServices[position]["service_name"].toString()

        if (dataListServices[position]["service_image"].toString() != "")
            Picasso.get().load(dataListServices[position]["service_image"].toString())
                .error(R.drawable.dummy_services)
                .into(holder.ivImage)
        else
            holder.ivImage.setImageResource(R.drawable.dummy_services)

      /*  holder.itemView.setOnClickListener {

            llHomeServices.visibility = View.GONE
            llSubPoints.visibility = View.VISIBLE

            serviceSelected = dataListServices[position]["service_name"].toString()
            serviceSelectedID = dataListServices[position]["_id"].toString()

            when (serviceSelected) {
                "Flat Tyre" -> {
                    llThree.visibility = View.GONE
                    llTwo.visibility = View.VISIBLE
                    setAnimationRight(llCarTwo, activity!!)
                    setAnimationLeft(llScooterTwo,activity!!)
                    topBarChanges!!.showGoneTopBar(false)
                    rlTopDrawer.visibility = View.VISIBLE
                    tvHeading.text = resources.getString(R.string.flat_tyre_worries)
                    tvSubheading.text = resources.getString(R.string.fix_on_the_spot)
                }
                "Jump Start" -> {
                    llThree.visibility = View.GONE
                    llTwo.visibility = View.VISIBLE
                    topBarChanges!!.showGoneTopBar(false)
                    rlTopDrawer.visibility = View.VISIBLE
                    tvHeading.text = resources.getString(R.string.dead_battery_worries)
                    tvSubheading.text = resources.getString(R.string.jump_start)
                }
                "Fuel Delivery" -> {
                    llThree.visibility = View.VISIBLE
                    llTwo.visibility = View.GONE
                    topBarChanges!!.showGoneTopBar(false)
                    rlTopDrawer.visibility = View.VISIBLE
                    tvHeading.text = resources.getString(R.string.empty_tanks_worries)
                    tvSubheading.text = resources.getString(R.string.deliver_real_quick)
                }
                "Burst Tyre" -> {
                    llThree.visibility = View.GONE
                    llTwo.visibility = View.VISIBLE
                    topBarChanges!!.showGoneTopBar(false)
                    rlTopDrawer.visibility = View.VISIBLE
                    tvHeading.text = resources.getString(R.string.burst_tyre_worries)
                    tvSubheading.text = resources.getString(R.string.help_you_fix)
                }
                "Towing" -> {
                    llThree.visibility = View.GONE
                    llTwo.visibility = View.VISIBLE
                    topBarChanges!!.showGoneTopBar(false)
                    rlTopDrawer.visibility = View.VISIBLE
                    tvHeading.text = resources.getString(R.string.double_trouble)
                    tvSubheading.text = resources.getString(R.string.we_will_connect_towing)
                }
                else -> {
                    llHomeServices.visibility = View.VISIBLE
                    llSubPoints.visibility = View.GONE
                    rlTopDrawer.visibility = View.GONE
                }
            }

        }*/
    }

    override fun getItemCount(): Int {
        return (dataListServices.size)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivImage = view.findViewById(R.id.ivImage) as ImageView
        var relativeLayout = view.findViewById(R.id.relativeLayout) as RelativeLayout
        var tvText = view.findViewById(R.id.tvText) as TextView
    }
}