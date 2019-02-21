package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.fobbu.member.android.R
import kotlinx.android.synthetic.main.inflate_ods_operation.view.*

class OdsOperationAdapter(var context:Context, var dataList: ArrayList<Map<String, Any>>):RecyclerView.Adapter<OdsOperationAdapter.OperationViewHolder>()
{
    var optionList=ArrayList<String>()

    lateinit var odsSubServiceAdapter:OdsSubServiceAdapter

    private var viewpool:RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OperationViewHolder
    {
        val view=LayoutInflater.from(context).inflate(R.layout.inflate_ods_operation,p0,false)

        viewpool=   view.rvOdsSubService.recycledViewPool

    return OperationViewHolder(view)
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

        odsSubServiceAdapter=OdsSubServiceAdapter(context,optionList)

        p0.rvOdsSubSrevice.layoutManager=LinearLayoutManager(context)

        p0.rvOdsSubSrevice.adapter=odsSubServiceAdapter

        p0.rvOdsSubSrevice.setRecycledViewPool(viewpool)

        p0.rvOdsSubSrevice.setHasFixedSize(true)

        p0.rvOdsSubSrevice.isNestedScrollingEnabled=false

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