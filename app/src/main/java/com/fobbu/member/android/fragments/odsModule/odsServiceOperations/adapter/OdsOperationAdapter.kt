package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R

class OdsOperationAdapter(var context:Context, var dataList: ArrayList<Map<String, Any>>):RecyclerView.Adapter<OdsOperationAdapter.OperationViewHolder>()
{
    var optionList=ArrayList<String>()


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OperationViewHolder
    {
    return OperationViewHolder(LayoutInflater.from(context).inflate(R.layout.inflate_ods_operation,p0,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: OperationViewHolder, p1: Int)
    {
        p0.tvTopUp.text=dataList[p1]["service_name"].toString()

        val map=dataList[p1]

        optionList=map.getValue("option") as ArrayList<String>

        println("option list:::::$optionList")


        p0.ivPlusListing.setOnClickListener {
            if (p0.rlOdsSubService.visibility==View.GONE)
                p0.rlOdsSubService.visibility=View.VISIBLE
            else
                p0.rlOdsSubService.visibility=View.GONE
        }
    }


    class OperationViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        var tvTopUp:TextView=view.findViewById(R.id.tvTopUpOperations)

        var ivPlusListing:ImageView=view.findViewById(R.id.ivOperationAdd)

        var rlOdsSubService:RelativeLayout=view.findViewById(R.id.rlOdsSubService)

        var rvOdsSubSrevice:RecyclerView=view.findViewById(R.id.rvOdsSubService)
    }
}