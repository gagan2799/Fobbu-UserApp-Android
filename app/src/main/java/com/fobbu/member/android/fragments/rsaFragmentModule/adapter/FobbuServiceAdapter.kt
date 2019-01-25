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