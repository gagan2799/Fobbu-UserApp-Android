package com.fobbu.member.android.activities.vehicleModule.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fobbu.member.android.R

class VehicleImageAdapter(var context:Context,var dataList:ArrayList<Any>) : RecyclerView.Adapter<VehicleImageAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.inflate_vehcile_images,null))
    }

    override fun getItemCount(): Int
    {
        return 4
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        if (dataList.isNotEmpty())
        {
            if (p1< dataList.size)
                Glide.with(context)
                   .load(dataList[p1])
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .skipMemoryCache(true)
                   .into(p0.imgDoc)

            else
               p0.imgDoc.setImageResource(R.drawable.photo_camera)
        }
        else
            p0.imgDoc.setImageResource(R.drawable.photo_camera)

    }


    // inner view holder class of the adapter
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var imgDoc = view.findViewById(R.id.ivVehicleImages) as ImageView
    }
}