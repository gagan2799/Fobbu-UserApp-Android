package com.fobbu.member.android.fragments.odsModule.odsFragment.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fobbu.member.android.R
import com.fobbu.member.android.utils.CommonClass
import com.squareup.picasso.Picasso

class OdsFragmentAdapter(var activity :Activity,var dataList: ArrayList<HashMap<String,Any>>/*,var dataList:ArrayList<HashMap<String, Any>>*/):RecyclerView.Adapter<OdsFragmentAdapter.OdsViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OdsViewHolder
    {
        return OdsViewHolder(LayoutInflater.from(activity).inflate(R.layout.inflate_ods_layout,p0,false))
    }

    override fun getItemCount(): Int
    {
        return dataList.size
    }

    override fun onBindViewHolder(holder: OdsViewHolder, position: Int)
    {
        if (isODD(position))
            holder.view.visibility=View.INVISIBLE

        else
            holder.view.visibility=View.VISIBLE

        if (position== dataList.size-2 || position== dataList.size-1)
            holder.viewBottom.visibility=View.INVISIBLE


        holder.tvText.text=dataList[position]["service_name"].toString()

        if (dataList[position]["service_image"].toString()!="")
        Picasso.get().load(dataList[position]["service_image"].toString())

            .placeholder(R.drawable.dummy_services)

            .error(R.drawable.dummy_services)

            .into(holder.ivImage)
        else
            holder.ivImage.setImageResource(R.drawable.dummy_services)
    }

    class OdsViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        var ivImage = view.findViewById(R.id.ivImage) as ImageView

        var relativeLayout = view.findViewById(R.id.relativeLayout) as RelativeLayout

        var tvText = view.findViewById(R.id.tvText) as TextView

        var view:View = view.findViewById(R.id.view)

        var viewBottom:View = view.findViewById(R.id.viewBottom)
    }

    private fun isODD(number:Int):Boolean
    {
        return (number % 2) != 0
    }
}