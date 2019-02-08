package com.fobbu.member.android.fragments.odsModule.odsServiceOperations.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fobbu.member.android.R

class OdsOperationAdapter(var context:Context):RecyclerView.Adapter<OdsOperationAdapter.OperationViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OperationViewHolder
    {
    return OperationViewHolder(LayoutInflater.from(context).inflate(R.layout.inflate_ods_operation,p0,false))
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(p0: OperationViewHolder, p1: Int) {

    }


    class OperationViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        var tvTopUp:TextView=view.findViewById(R.id.tvTopUpOperations)

        var ivPlusListing:ImageView=view.findViewById(R.id.ivOperationAdd)
    }
}